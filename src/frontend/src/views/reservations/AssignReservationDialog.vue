<template>
	<v-dialog v-model="dialog" max-width="400">
		<template v-slot:activator="{ props: activatorProps }">
			<v-btn
				v-if="!props.maintenance"
				v-bind="activatorProps"
				class="mr-2 pa-1"
				density="comfortable"
				variant="outlined"
				size="small"
				icon="mdi-account-plus"
				@click="initialize"
			>
				<v-icon/>
				<v-tooltip
					activator="parent"
					location="bottom"
					offset="5"
					text="Reserva"
				/>
			</v-btn>

			<v-btn
				v-else
				v-bind="activatorProps"
				class="mr-2 pa-1"
				density="comfortable"
				variant="outlined"
				size="small"
				icon="mdi-cog"
				@click="initialize"
			>
				<v-icon/>
				<v-tooltip
					activator="parent"
					location="bottom"
					offset="5"
					text="Manutenção"
				/>
			</v-btn>
		</template>

		<v-card prepend-icon="mdi-briefcase" :title="title">
			<v-card-text>
				<!-- Persons list !-->
				<v-autocomplete
					v-if="!props.maintenance"
					label="IST ID da Pessoa"
					v-model="istIdSelected"
					:items="personIds"
					:rules="istIdRules"
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

const emit = defineEmits(['reservation-assigned'])

const props = defineProps({
	maintenance: {
		type: Boolean,
		default: false,
	},
	resource: Object,
	person: Object
})

const istIdSelected = ref('')
const resouceIdSelected = ref('')

const dateInterval = ref([])
const validReservation = ref(false)

const newReservation = reactive<ReservationDto>({
	startDate: '',
	finishDate: '',
})

const personIds = reactive([])
const resourceIds = reactive([])


function initialize() {
	dateInterval.value.splice(0,dateInterval.value.length)
	updateTextFields()
	
	if (props.maintenance) {
		title.value = 'Manutenção'
		label.value = 'Defina a duração da manutenção no calendário:'
	} else { 
		title.value = 'Reserva'
		label.value = 'Defina a duração da reserva no calendário:'

		istIdSelected.value = ''
		getPersonIds()
	}
}

function updateTextFields() {
	if (dateInterval.value.length == 0) { 
		// No date selected
		newReservation.startDate = ''
		newReservation.finishDate = ''
		valid.value = false

		return
	}

	newReservation.startDate = getStringFromDate(dateInterval.value[0])
	newReservation.finishDate = getStringFromDate(dateInterval.value[dateInterval.value.length-1])

	validReservation.value = !intervalOverlapsReservations(newReservation)

	valid.value = (props.maintenance == true || (istIdSelected.value != '' && istIdSelected.value != null)) && validReservation.value == true
}

const assignReservation = async () => {
	if (props.maintenance) {
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


// True if the date doesnt overlap with the reservations
function doesntOverlapReservations(date: Date): boolean {
	for (const reservation of props.resource.reservations) {
		if (reservation.state == 'FINISHED' || reservation.state == 'CANCELLED') {
			continue
		}

		var reservationStartDate = getDateFromString(reservation.startDate)
		var reservationFinishDate = getDateFromString(reservation.finishDate)

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

		var intervalStartDate = getDateFromString(interval.startDate)
		var intervalFinishDate = getDateFromString(interval.finishDate)

		var reservationStartDate = getDateFromString(reservation.startDate)
		var reservationFinishDate = getDateFromString(reservation.finishDate)

		if (intervalStartDate <= reservationFinishDate 
			&& intervalFinishDate >= reservationStartDate) {
			return true
		}
	}

	return false
}

const istIdRules = [
	(v: string) => !!v || 'Insira o IST ID',
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
