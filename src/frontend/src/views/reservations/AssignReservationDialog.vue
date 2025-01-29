<template>
	<v-dialog v-model="dialog" max-width="400">
		<template v-slot:activator="{ props: activatorProps }">
			<v-btn
				v-bind="activatorProps"
				class="mr-2 pa-1"
				density="comfortable"
				variant="outlined"
				size="small"
				:icon="icon"
				@click="initialize"
			>
				<v-icon/>
				<v-tooltip
					activator="parent"
					location="bottom"
					offset="5"
					:text="title"
				/>
			</v-btn>
		</template>

		<v-card prepend-icon="mdi-briefcase" :title="title">
			<v-card-text>
				<!-- Persons list !-->
				<v-autocomplete
					v-if="props.mode == 'reservation' && props.resource != null"
					label="IST ID da Pessoa"
					v-model="istIdSelected"
					:items="personIds"
					:rules="idRules"
					@update:model-value="updateTextFields"
					required
					class="mb-2"
				></v-autocomplete>

				<v-autocomplete
					v-else-if="props.mode == 'reservation' && props.person != null"
					label="ID Recurso"
					v-model="resourceIdSelected"
					:items="resourceIds"
					:rules="idRules"
					@update:model-value="updateTextFields"
					required
					class="mb-2"
				></v-autocomplete>
				
				<!-- Date selection !-->
				<p class="ml-2 mt-2 mb-3">{{ label }}</p>

				<div class="d-flex justify-center">
					<v-date-picker 
						v-model="dateInterval"
						multiple="range"
						:min="getRelativeDate(-1)"
						:max="getRelativeDate(365)"
						:allowed-dates="doesntOverlapReservations"
						:hide-header="true"
						color="primary"
						style="border: 1px solid #ddd;"
						@click="updateTextFields"
					/>
				</div>

				<div class="d-flex mt-3 ml-6">
					<p class="mb-1">Duração:</p>
					<Transition name="fade">
						<p 
							v-if="newReservation.startDate != '' && validReservation == true"
							class="ml-2 pl-3 pr-3" 
							style="border: 1px solid #ddd; border-radius: 15px; text-align: center;"
						>{{ newReservation.startDate }} - {{ newReservation.finishDate}}
						</p>
					</Transition>
				</div>

			</v-card-text>

			<v-divider></v-divider>

			<v-card-actions>
				<v-spacer></v-spacer>

				<v-btn 
					text="Cancelar" 
					variant="plain" 
					@click="
					dialog = false;
					"
				></v-btn>

				<v-btn
					text="Adicionar"
					color="primary"
					:disabled="!valid"
					@click="
					dialog = false;
					assignReservation()
					"
				></v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script setup lang="ts">

import { ref, reactive } from 'vue'

import RemoteService from '@/services/RemoteService'

import type ReservationDto from '@/models/dtos'
import type ResourceDto from '@/models/dtos'
import type PersonDto from '@/models/dtos'

import { validDate } from '@/lib/regExpressions'
import { getDateFromString, getStringFromDate, getRelativeDate } from '@/lib/dateUtils'


const dialog = ref(false)
const valid = ref(false)

const title = ref('')
const label = ref('')
const icon = ref('')

const emit = defineEmits(['reservation-assigned'])

const props = defineProps({
	mode: {
		type: String,
		default: 'reservation',
	},
	resource: {
		type: Object,
		default: null
	},
	person: {
		type: Object,
		default: null
	}
})

const istIdSelected = ref('')
const resourceIdSelected = ref('')

const dateInterval = ref([])
const validReservation = ref(false)

const newReservation = reactive<ReservationDto>({
	startDate: '',
	finishDate: '',
})

const personIds = reactive([])
const resourceIds = reactive([])

initialize()

function initialize() {
	dateInterval.value.splice(0,dateInterval.value.length)
	updateTextFields()
	
	if (props.mode == 'maintenance') {
		title.value = 'Manutenção'
		label.value = 'Defina a duração da manutenção no calendário:'
		icon.value = 'mdi-cog'
	} else { 
		title.value = 'Reserva'
		label.value = 'Defina a duração da reserva no calendário:'

		istIdSelected.value = ''
		resourceIdSelected.value = ''

		if (props.resource != null) {
			icon.value = 'mdi-account-plus'
			getPersonIds()
		} else {
			icon.value = 'mdi-briefcase-plus'
			getResourceIds()
		}
	}
}

function updateTextFields() {
	if (dateInterval.value.length == 0) {
		newReservation.startDate = ''
		newReservation.finishDate = ''
		valid.value = false

		return
	}

	newReservation.startDate = getStringFromDate(dateInterval.value[0])
	newReservation.finishDate = getStringFromDate(dateInterval.value[dateInterval.value.length-1])

	validReservation.value = !intervalOverlapsReservations(newReservation)

	valid.value = (props.mode == 'maintenance' || (istIdSelected.value != '' && istIdSelected.value != null)) && validReservation.value == true
}

const assignReservation = async () => {
	if (props.mode == 'maintenance') {
		await RemoteService.assignMaintenance(newReservation, props.resource.id)
	} else {
		await RemoteService.assignReservation(newReservation, istIdSelected.value, props.resource.id)
	}
	
	emit('reservation-assigned')
}


async function getPersonIds() {
	personIds.splice(0, personIds.length)
	RemoteService.getPersons().then(async (data) => {
		data.forEach((person: PersonDto) => {
			personIds.push(person.id)
		})
	})
}

async function getResourceIds() {
	resourceIds.splice(0, resourceIds.length)
	RemoteService.getResources().then(async (data) => {
		data.forEach((resource: ResourceDto) => {
			resourceIds.push(resource.id)
		})
	})
}

// True if the date doesnt overlap with the reservations
function doesntOverlapReservations(date: Date): boolean {
	if (props.resource == null) {
		return false
	}
	const reservationsList = props.resource.reservations

	for (const reservation of reservationsList) {
		if (reservation.state == 'FINISHED' || reservation.state == 'CANCELLED') {
			continue
		}

		const reservationStartDate = getDateFromString(reservation.startDate)
		const reservationFinishDate = getDateFromString(reservation.finishDate)

		if (date >= reservationStartDate && date <= reservationFinishDate) {
			return false
		}
	}

	return true
}


// True if the interval overlaps with the reservations
function intervalOverlapsReservations(interval): boolean {
	for (const reservation of props.resource.reservations) {
		if (reservation.state == 'FINISHED' || reservation.state == 'CANCELLED') {
			continue
		}

		const intervalStartDate = getDateFromString(interval.startDate)
		const intervalFinishDate = getDateFromString(interval.finishDate)

		const reservationStartDate = getDateFromString(reservation.startDate)
		const reservationFinishDate = getDateFromString(reservation.finishDate)

		if (intervalStartDate <= reservationFinishDate 
			&& intervalFinishDate >= reservationStartDate) {
			return true
		}
	}

	return false
}

const idRules = [
	(v: string) => !!v || 'Insira o ID',
]

</script>

<style scoped>

.fade-enter-active, .fade-leave-active {
	transition: opacity 0.3s ease;
}

.fade-enter-from, .fade-leave-to {
	opacity: 0;
}


</style>
