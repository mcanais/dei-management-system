<template>
	<div>
		<v-dialog v-model="dialog" max-width="400">
			<template v-slot:activator="{ props: activatorProps }">
				<v-icon
					v-if="!props.maintenance"
					v-bind="activatorProps"
					class="mr-2"
					@click="initialize"
				>mdi-account-plus</v-icon>

				<v-icon
					v-else
					v-bind="activatorProps"
					class="mr-2"
					@click="initialize"
				>mdi-cog</v-icon>
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
					<p class="ml-2 mt-2 mb-2">{{ label }}</p>

					<v-date-picker 
						v-model="dateInterval"
						multiple="range"
						:min="getRelativeDate(-1)"
						:allowed-dates="doesntOverlapReservations"
						:hide-header="true"
						@click="updateTextFields"
					/>

					<Transition name="fade">
						<p class="reservation-label" v-if="newReservation.startDate != ''">{{ newReservation.startDate }} - {{ newReservation.finishDate}} </p>
					</Transition>

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
						text="Salvar"
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
	</div>
</template>

<script setup lang="ts">

import { ref, reactive } from 'vue'
import type ReservationDto from '@/models/dtos'
import RemoteService from '@/services/RemoteService'
import { validDate } from '@/lib/regExpressions'
import { getDateFromString, getStringFromDate, getRelativeDate } from '@/lib/dateUtils'


const dialog = ref(false)
const valid = ref(false)


const title = ref('')
const label = ref('')

const emit = defineEmits(['reservation-assigned'])

const props = defineProps({
	resource: Object,
	maintenance: Boolean,
})

const istIdSelected = ref('')

const dateInterval = ref([])

const newReservation = reactive<ReservationDto>({
	startDate: '',
	finishDate: '',
})

const personIds = reactive([])


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

	// TODO: extra validation step so the date interval doesnt overlap

	valid.value = (props.maintenance == true || (istIdSelected.value != '' && istIdSelected.value != null))
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


function doesntOverlapReservations(date: Date): boolean {
	for (const reservation of props.resource.reservations) {
		// TODO: skip if the reservation is finished or cancelled

		var reservationStartDate = getDateFromString(reservation.startDate)
		var reservationFinishDate = getDateFromString(reservation.finishDate)

		if (date >= reservationStartDate && date <= reservationFinishDate) {
			return false
		}
	}

	return true
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


.reservation-label {
	border: 1px solid #ddd;
	border-radius: 15px;
	margin: 5px 35px;
	text-align: center;
}

</style>
