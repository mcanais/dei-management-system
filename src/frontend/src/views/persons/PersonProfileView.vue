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

			<v-divider class="mb-10 mt-2"></v-divider>
			<UpdatePersonDialog 
				class="mb-1" 
				:person="tempPerson" 
				:buttonText="true" 
				:edit="true"
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
				<h2 class="text-left ml-2 mb-3">Reservas</h2>
				<v-data-table
					:headers="headers"
					:items="tempPerson.reservations"
					:hide-default-footer="true"
					class="text-left"
				>
					<template v-slot:[`item.actions`]="{ item }">
						<v-icon @click="cancelReservation(item)">mdi-delete</v-icon>
					</template>
				</v-data-table>
			</div>

			<div class="historic-container">
				<h2 class="text-left ml-2 mb-3">Histórico de Reservas</h2>
			</div>
		</div>
	</div>

</template>


<script setup lang="ts">

import { ref, reactive } from 'vue'

import RemoteService from '@/services/RemoteService'

import type PersonDto from '@/models/dtos'
import { personRoles } from '@/models/person/PersonRoles'
import { getItemValue, fuzzySearch } from '@/lib/utils'

import UpdatePersonDialog from '@/views/persons/UpdatePersonDialog.vue'
import RemovePersonDialog from '@/views/persons/RemovePersonDialog.vue'


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

const headers = [
	{ title: 'Id Recurso', value: 'assignedResourceId', key: 'resourceName' },
	{ title: 'Data Inicial', value: 'startDate', key: 'startDate' },
	{ title: 'Data Final', value: 'finishDate', key: 'finishDate' },
	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false }
]


getPersonData()

async function getPersonData() {
	RemoteService.getPerson(props.personId).then(async (data) => {
		tempPerson.id = data.id
		tempPerson.name = data.name
		tempPerson.email = data.email
		tempPerson.role = data.role
		tempPerson.reservations = data.reservations
	})
}

function cancelReservation(reservation) {
	RemoteService.cancelReservation(reservation).then(() => {
		getPersonData()
	})
}

</script>

<style scoped>

.profile-container {
	display: flex;
	justify-content: space-between;
	gap: 15px;
}

.left-container {
	padding: 20px;
	flex-grow: 1;
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
