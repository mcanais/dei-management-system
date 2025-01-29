<template>
	<div class="d-flex flex-column align-start">
		<v-btn text="Voltar" 
			@click="router.push(`/resources`)" 
			class="mb-2 pa-1" 
			density="comfortable" 
			variant="text" 
			prepend-icon="mdi-subdirectory-arrow-left"
		/>
		<h2 class="text-left ml-1 mb-3">Perfil Recurso</h2>
	</div>

	<div class="profile-container">
		<div class="left-container text-left">
			<img src="@/images/resource-icon.svg" class="profile-icon"></img>
			<p><strong>{{ tempResource.name }}</strong> </p>
			<p>Id Recurso: {{ tempResource.id }}</p>
			<v-divider class="mb-5 mt-2"></v-divider>
			<p class="pl-2 mb-1"><strong>Localização:</strong> {{ tempResource.location }}</p>
			<p class="pl-2 mb-5"><strong>Tipo:</strong> {{ getItemValue(resourceTypes, tempResource.type) }}</p>

			<v-divider class="mb-8 mt-2"></v-divider>
			<CreateResourceDialog 
				class="mb-1" 
				:resource="tempResource" 
				:buttonText="true" 
				@resource-updated="getResourceData"
			/>
			<RemoveObjectDialog 
				@remove-object="removeResource(tempResource)"
				:buttonText="true"
				title="Remover Recurso"
				text="Tem a certeza que quer remover este recurso?"
				icon="mdi-briefcase"
			/>
		</div>

		<div class="right-container">

			<div class="current-state-container">
				<h2 class="text-left ml-2 mb-3">Estado Atual</h2>

				<div class="border-sm rounded-lg pa-3 d-flex flex-column align-start overflow-auto">
					<div class="d-flex align-center ga-2">
						<ResourceStateChip :state="tempResource.state"/>
						<RemoveObjectDialog 
							v-if="tempResource.state != 'AVAILABLE'"
							@remove-object="cancelReservation(currentReservation)"
							title="Cancelar Reserva"
							text="Tem a certeza que quer cancelar esta reserva?"
							icon="mdi-calendar"
							submitText="Sim"
							cancelText="Não"
						/>
					</div>

					<div v-if="tempResource.state == 'AVAILABLE'">
						<p class="mt-3 ml-1 text-left">Recurso atualmente disponível.</p>

						<p v-if="validReservations.length != 0" 
							class="mt-1 ml-1 text-left"
							>Próxima reserva: {{ nextReservation.startDate }} - {{ nextReservation.finishDate }}</p>

						<p v-else class="mt-1 ml-1 text-left">Sem reservas futuras.</p>
					</div>

					<div v-else-if="tempResource.state == 'INUSE'">
						<div class="d-flex mt-3 ml-1 text-left ga-1">
							<p> Recurso atualmente em uso por:</p>
							<button 
								class="id-button" 
								@click="router.push(`/persons/${currentReservation.assignedPersonId}`)"
							>{{ currentReservation.assignedPersonId }}</button>
						</div>
						<p class="mt-2 ml-1 text-left">Duração: {{ currentReservation.startDate }} - {{ currentReservation.finishDate }}</p>
					</div>

					<div v-else-if="tempResource.state == 'MAINTENANCE'">
						<p class="mt-3 ml-1 text-left">Recurso atualmente em manutenção.</p>
						<p class="mt-2 ml-1 text-left">Duração: {{ currentReservation.startDate }} - {{ currentReservation.finishDate }}</p>
					</div>
				</div>
			</div>

			<div class="reservations-container">
				<div class="d-flex align-center mb-3">
					<h2 class="text-left ml-2">{{ reservationsTitle }}</h2>
					<v-spacer/>
					<AssignReservationDialog @reservation-assigned="getResourceData" :resource="tempResource" mode="reservation"/>
					<AssignReservationDialog @reservation-assigned="getResourceData" :resource="tempResource" mode="maintenance"/>
				</div>

				<v-data-table
					:headers="headersReservations"
					:items="validReservations"
					no-data-text="Sem reservas"
					class="text-left w-auto"
				>
					<template v-slot:[`item.type`]="{ item }">
						<ReservationTypeChip :type="item.type" :assignedPersonId="item.assignedPersonId"/>
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
					<template v-slot:[`item.type`]="{ item }">
						<ReservationTypeChip :type="item.type"/>
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

import type ResourceDto from '@/models/dtos'
import type ReservationDto from '@/models/dtos'

import { resourceTypes } from '@/models/resource/ResourceTypes'
import { getItemValue, fuzzySearch } from '@/lib/utils'
import { getDateFromString, getStringFromDate, getRelativeDate, sortByDate } from '@/lib/dateUtils'

import CreateResourceDialog from '@/views/resources/CreateResourceDialog.vue'
import RemoveObjectDialog from '@/components/RemoveObjectDialog.vue'
import AssignReservationDialog from '@/views/reservations/AssignReservationDialog.vue'

import ReservationStateChip from '@/components/chips/ReservationStateChip.vue'
import ResourceStateChip from '@/components/chips/ResourceStateChip.vue'
import ReservationTypeChip from '@/components/chips/ReservationTypeChip.vue'

const search = ref('')
const router = useRouter()

const props = defineProps({
	resourceId: String
})

const tempResource = reactive<ResourceDto>({
	id: '',
	name: '',
	type: '',
	state: '',
	location: '',
	reservations: [],
})


const currentReservation = ref<ReservationDto>({})
const nextReservation = ref<ReservationDto>({})

const validReservations = reactive<ReservationDto[]>([])
const historicReservations = reactive<ReservationDto[]>([])

const reservationsTitle = ref('')
const historicTitle = ref('')


const headersReservations = [
	{ title: 'Tipo', value: 'type', key: 'type' },
	{ title: 'Data Inicial', value: 'startDate', key: 'startDate', sort: sortByDate },
	{ title: 'Data Final', value: 'finishDate', key: 'finishDate', sort: sortByDate },
	{ title: 'Estado', value: 'state', key: 'state' },
	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false }
]

const headersHistoric = [
	{ title: 'Tipo', value: 'type', key: 'type' },
	{ title: 'Data Inicial', value: 'startDate', key: 'startDate', sort: sortByDate },
	{ title: 'Data Final', value: 'finishDate', key: 'finishDate', sort: sortByDate },
	{ title: 'Estado', value: 'state', key: 'state' }
]


getResourceData()

async function getResourceData() {
	RemoteService.getResource(props.resourceId).then(async (data) => {
		tempResource.id = data.id
		tempResource.name = data.name
		tempResource.type = data.type
		tempResource.state = data.state
		tempResource.location = data.location

		tempResource.reservations = data.reservations
		updateReservations()
	}).catch((error) => {
		console.error('Error getting resource Data', error)
	})
}


function removeResource(resource) {
	RemoteService.deleteResource(resource).then(() => {
		router.push(`/resources`)
	})
}

async function cancelReservation(reservation) {
	RemoteService.cancelReservation(reservation).then(async (data) => {
		getResourceData()
	})
}

function updateReservations() {
	validReservations.splice(0, validReservations.length)
	historicReservations.splice(0, historicReservations.length)
	nextReservation.value = {}

	tempResource.reservations.forEach((reservation: ReservationDto) => {

		if (reservation.state == 'ACTIVE') {
			currentReservation.value = reservation
			validReservations.push(reservation)
		} else if (reservation.state == 'PENDING') {
			validReservations.push(reservation)

			if (!nextReservation.value.startDate || getDateFromString(reservation.startDate) < getDateFromString(nextReservation.value.startDate)) {
				nextReservation.value = reservation
			}
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
	margin-bottom: 10px;
}

.current-state-container, .reservations-container, .historic-container {
	flex-grow: 1;
	padding: 20px;
	border-radius: 8px;
	border: 1px solid #ddd;
	overflow-x: auto;
}

</style>
