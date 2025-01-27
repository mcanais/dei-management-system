<template>
	<div class="d-flex flex-column align-start">
		<v-btn text="Voltar" @click="router.push(`/persons`)" class="mb-2 pa-1" density="comfortable" variant="text" prepend-icon="mdi-subdirectory-arrow-left"/>
		<h2 class="text-left ml-1 mb-3">Perfil Pessoa</h2>
	</div>

	<div class="profile-container">
		<div class="left-container text-left">
			<img src="@/images/profile-icon.svg" class="profile-icon"></img>
			<p><strong>{{ tempPerson.name }}</strong> </p>
			<p>@{{ tempPerson.id }}</p>
			<v-divider class="mb-5 mt-2"></v-divider>
			<p class="pl-2 mb-1"><strong>Email:</strong> {{ tempPerson.email }}</p>
			<p class="pl-2 mb-5"><strong>Função:</strong> {{ getItemValue(personRoles, tempPerson.role) }}</p>

			<v-divider class="mb-8 mt-2"></v-divider>
			<CreatePersonDialog 
				class="mb-1" 
				:person="tempPerson" 
				:buttonText="true" 
				@person-updated="getPersonData"
			/>
			<RemoveObjectDialog 
				@remove-object="removePerson(tempPerson)"
				:buttonText="true"
				title="Remover Pessoa"
				text="Tem a certeza que quer remover esta pessoa?"
				icon="mdi-account"
			/>
		</div>

		<div class="right-container">
			<div class="reservations-container">
				<h2 class="text-left ml-2 mb-3">{{ reservationsTitle }}</h2>

				<v-data-table
					:headers="headersReservations"
					:items="validReservations"
					no-data-text="Sem reservas"
					class="text-left w-auto"
				>
					<template v-slot:[`item.assignedResourceId`]="{ item }">
						<button 
							class="id-button" 
							@click="router.push(`/resources/${item.assignedResourceId}`)"
						>{{ item.assignedResourceId }}</button>
					</template>

					<template v-slot:[`item.state`]="{ item }">
						<ReservationStateChip :state="item.state"/>
					</template>

					<template v-slot:[`item.actions`]="{ item }">
						<RemoveObjectDialog 
							@remove-object="cancelReservation(item)"
							title="Cancelar Reserva"
							text="Tem a certeza que quer cancelar esta reserva?"
							icon="mdi-calendar"
							submitText="Sim"
							cancelText="Não"
						/>
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
					<template v-slot:[`item.assignedResourceId`]="{ item }">
						<button 
							class="id-button" 
							@click="router.push(`/resources/${item.assignedResourceId}`)"
						>{{ item.assignedResourceId }}</button>
					</template>

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
import { useRouter } from 'vue-router'

import RemoteService from '@/services/RemoteService'

import type PersonDto from '@/models/dtos'
import type ReservationDto from '@/models/dtos'

import { personRoles } from '@/models/person/PersonRoles'
import { getItemValue, fuzzySearch } from '@/lib/utils'
import { getDateFromString, getStringFromDate, getRelativeDate, sortByDate } from '@/lib/dateUtils'

import CreatePersonDialog from '@/views/persons/CreatePersonDialog.vue'
import RemoveObjectDialog from '@/components/RemoveObjectDialog.vue'
import ReservationStateChip from '@/components/chips/ReservationStateChip.vue'

const search = ref('')
const router = useRouter()

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
	{ title: 'Id Recurso', value: 'assignedResourceId', key: 'assignedResourceId' },
	{ title: 'Data Inicial', value: 'startDate', key: 'startDate', sort: sortByDate },
	{ title: 'Data Final', value: 'finishDate', key: 'finishDate', sort: sortByDate },
	{ title: 'Estado', value: 'state', key: 'state' },
	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false }
]

const headersHistoric = [
	{ title: 'Id Recurso', value: 'assignedResourceId', key: 'assignedResourceId' },
	{ title: 'Data Inicial', value: 'startDate', key: 'startDate', sort: sortByDate },
	{ title: 'Data Final', value: 'finishDate', key: 'finishDate', sort: sortByDate },
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


function removePerson(person) {
	RemoteService.deletePerson(person).then(() => {
		router.push(`/persons`)
	})
}

async function cancelReservation(reservation) {
	RemoteService.cancelReservation(reservation).then(async (data) => {
		getPersonData()
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
	overflow-x: auto;
	white-space: nowrap;
}

.profile-icon {
	width: 80px;
	height: 80px;
}

.reservations-container, .historic-container {
	flex-grow: 1;
	padding: 20px;
	border-radius: 8px;
	border: 1px solid #ddd;
}

</style>
