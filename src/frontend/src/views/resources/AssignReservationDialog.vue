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

			<v-form v-model="valid" @submit.prevent="onSubmit" ref="form">
				<v-card prepend-icon="mdi-briefcase" :title="title">
					<v-card-text>
						<!-- Persons list !-->
						<v-autocomplete
							v-if="!props.maintenance"
							label="IST ID da Pessoa"
							v-model="istIdSelected"
							:items="personIds"
							:rules="istIdRules"
							auto-select-first
							required
							class="mb-3"
						></v-autocomplete>
						
						<!-- Date selection !-->
						<!-- TODO: move this into a date picker !-->
						<p class="ml-2 mt-2 mb-2">{{ label }}</p>
						<v-divider></v-divider>
						<v-text-field 
							label="Data Inicial" 
							v-model="newReservation.startDate"
							:rules="startDateRules"
							ref="startDateTextField"
							@input="validateTextFields"
							required 
							class="mb-3"
						></v-text-field>

						<v-text-field 
							label="Data Final" 
							v-model="newReservation.finishDate"
							:rules="finishDateRules"
							ref="finishDateTextField"
							@input="validateTextFields"
							required 
						></v-text-field>


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
							type="submit"
							@click="
							dialog = false;
							assignReservation()
							"
						></v-btn>
					</v-card-actions>
				</v-card>
			</v-form>
		</v-dialog>
	</div>
</template>

<script setup lang="ts">

import { ref, reactive } from 'vue'
import type ReservationDto from '@/models/dtos'
import RemoteService from '@/services/RemoteService'
import { validDate } from '@/lib/regExpressions'
import { getCurrentDay, getTomorrowDay, isPastDate, overlappingReservations } from '@/lib/dateUtils'


const dialog = ref(false)
const valid = ref(false)
const form = ref(null)

const startDateTextField = ref(null)
const finishDateTextField = ref(null)

const title = ref('')
const label = ref('')

const emit = defineEmits(['reservation-assigned'])

const props = defineProps({
	resource: Object,
	maintenance: Boolean,
})

const istIdSelected = ref('')

const newReservation = reactive<ReservationDto>({
	startDate: '',
	finishDate: '',
})

const personIds = reactive([])


function initialize() {
	newReservation.startDate = getCurrentDay()
	newReservation.finishDate = getTomorrowDay()
	
	if (props.maintenance) {
		title.value = 'Manutenção'
		label.value = 'Defina a duração da manutenção:'
	} else { 
		title.value = 'Reserva'
		label.value = 'Defina a duração da reserva:'

		istIdSelected.value = ''
		getPersonIds()
	}

	validateTextFields()
}

const assignReservation = async () => {
	const {valid, errors} = await form.value?.validate()
	
	if (!valid) {
		console.log(errors)
		return
	}

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

function onSubmit() {
	return !valid
}


function validateTextFields() {
	form.value?.validate()
}

const istIdRules = [
	(v: string) => !!v || 'Insira o IST ID',
]

const startDateRules = [
	(v: string) => !!v || 'Insira a data',
	(v: string) => validDate(v) || 'Data inválida',
	(v: string) => !isPastDate(v,getCurrentDay()) || 'Data passada inválida',
	(v: string) => !overlappingReservations(props.resource.reservations, newReservation) || 'Este recurso já está reservado para esta data'
]

const finishDateRules = [
	(v: string) => !!v || 'Insira a data',
	(v: string) => validDate(v) || 'Data inválida',
	(v: string) => !isPastDate(v,newReservation.startDate) || 'Data final tem de vir depois de data inicial',
	(v: string) => !overlappingReservations(props.resource.reservations, newReservation) || 'Este recurso já está reservado para esta data'
]

</script>
