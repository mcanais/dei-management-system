<template>
	<h2 class="text-left ml-1 mb-3">Perfil Pessoa</h2>

	<div class="profile-container">
		<div class="left-container text-left">
			<img src="@/images/profile-icon.svg" class="profile-icon"></img>
			<p><strong>{{ tempPerson.name }}</strong> </p>
			<p>@{{ tempPerson.id }}</p>
			<v-divider class="mb-5 mt-2"></v-divider>
			<p class="pl-2 mb-1"><strong>Email:</strong> {{ tempPerson.email }}</p>
			<p class="pl-2 mb-5"><strong>Função:</strong> {{ getItemValue(personRoles, tempPerson.role) }}</p>

			<v-divider class="mb-8 mt-2"></v-divider>
			<UpdatePersonDialog 
				class="mb-1" 
				:person="tempPerson" 
				:buttonText="true" 
				@person-updated="getPersonData"
			/>
			<RemovePersonDialog 
				:person="tempPerson" 
				:buttonText="true"
				@person-updated="getPersonData"
			/>
		</div>

		<div class="right-container">
			<div class="reservations-container">
				<h2 class="text-left ml-2 mb-3">{{ reservationsTitle }}</h2>

				<v-data-table
					:headers="headersReservations"
					:items="validReservations"
					:min="getRelativeDate(-1)"
					class="text-left w-auto"
					no-data-text="Sem reservas"
					style="flex-grow: 1;"
				>
					<template v-slot:[`item.actions`]="{ item }">
						<CancelReservationDialog @reservation-cancelled="getPersonData" :reservation="item"/>
					</template>

					<template v-slot:[`item.state`]="{ item }">
						<ReservationStateChip :state="item.state"/>
					</template>
				</v-data-table>
			</div>

			<div class="historic-container">
				<h2 class="text-left ml-2 mb-3">{{ historicTitle }}</h2>
				<v-data-table
					:headers="headersHistoric"
					:items="historicReservations"
					no-data-text="Sem histórico de reservas"
					class="text-left"
				>
					<template v-slot:[`item.state`]="{ item }">
						<ReservationStateChip :state="item.state"/>
					</template>
				</v-data-table>
			</div>
		</div>
	</div>

</template>


<script setup lang="ts">

import { ref, reactive } from 'vue'

import RemoteService from '@/services/RemoteService'

import type PersonDto from '@/models/dtos'
import type ReservationDto from '@/models/dtos'
import { personRoles } from '@/models/person/PersonRoles'
import { getItemValue, fuzzySearch } from '@/lib/utils'
import { getDateFromString, getStringFromDate, getRelativeDate } from '@/lib/dateUtils'

import UpdatePersonDialog from '@/views/persons/UpdatePersonDialog.vue'
import RemovePersonDialog from '@/views/persons/RemovePersonDialog.vue'
import ReservationStateChip from '@/views/reservations/ReservationStateChip.vue'
import CancelReservationDialog from '@/views/reservations/CancelReservationDialog.vue'

const search = ref('')

const props = defineProps({
	personId: String
})

const tempPerson = reactive<PersonDto>({
	id: '',
	name: '',
	email: '',
	role: '',
	reservations: [],
})


const validReservations = reactive<ReservationDto[]>([])
const historicReservations = reactive<ReservationDto[]>([])

const reservationsTitle = ref('')
const historicTitle = ref('')


const headersReservations = [
	{ title: 'Id Recurso', value: 'assignedResourceId', key: 'resourceName' },
	{ title: 'Data Inicial', value: 'startDate', key: 'startDate' },
	{ title: 'Data Final', value: 'finishDate', key: 'finishDate' },
	{ title: 'Estado', value: 'state', key: 'state' },
	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false }
]

const headersHistoric = [
	{ title: 'Id Recurso', value: 'assignedResourceId', key: 'resourceName' },
	{ title: 'Data Inicial', value: 'startDate', key: 'startDate' },
	{ title: 'Data Final', value: 'finishDate', key: 'finishDate' },
	{ title: 'Estado', value: 'state', key: 'state' }
]


getPersonData()

async function getPersonData() {
	RemoteService.getPerson(props.personId).then(async (data) => {
		tempPerson.id = data.id
		tempPerson.name = data.name
		tempPerson.email = data.email
		tempPerson.role = data.role

		tempPerson.reservations = data.reservations
		updateReservations()
	}).catch((error) => {
		console.error('Error getting person Data', error)
	})
}


function updateReservations() {
	validReservations.splice(0, validReservations.length)
	historicReservations.splice(0, historicReservations.length)

	tempPerson.reservations.forEach((reservation: ReservationDto) => {
		if (reservation.state == 'ACTIVE' || reservation.state == 'PENDING') {
			validReservations.push(reservation)
		} else {
			historicReservations.push(reservation)
		}
	})

	reservationsTitle.value = `Reservas (${validReservations.length})`
	historicTitle.value = `Histórico de Reservas (${historicReservations.length})`
}


</script>

<style scoped>

.profile-container {
	display: flex;
	justify-content: space-between;
	flex-wrap: wrap;
	gap: 10px;
}

.left-container {
	flex-grow: 1;
	padding: 20px;
	border-radius: 8px;
}

.right-container {
	flex-grow: 1;
	display: flex;
	flex-direction: column;
	gap: 15px;
}

.profile-icon {
	width: 80px;
	height: 80px;
}

.information-container, .reservations-container, .historic-container {
	flex-grow: 1;
	padding: 20px;
	border-radius: 8px;
	border: 1px solid #ddd;
}

</style>
