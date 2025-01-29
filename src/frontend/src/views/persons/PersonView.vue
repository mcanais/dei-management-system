<template>
	<v-row align="center" class="mb-2">
		<v-col>
			<h2 class="text-left ml-1">Pessoal</h2>
		</v-col>

		<v-col cols="auto">
			<CreatePersonDialog @person-updated="getPersons" />
		</v-col>
	</v-row>

	<v-text-field
		v-model="search"
		label="Procurar"
		prepend-inner-icon="mdi-magnify"
		variant="outlined"
		hide-details
		single-line
	></v-text-field>
	

	<v-data-table
		:headers="headers"
		:items="persons"
		:search="search"
		:custom-filter="fuzzySearch"
		no-data-text="Ainda não existem pessoas na base de dados"
		class="text-left"
	>
		<template v-slot:[`item.id`]="{ item }">
			<button 
				class="id-button" 
				@click="router.push(`/persons/${item.id}`)"
			>{{ item.id }}</button>
		</template>

		<template v-slot:[`item.role`]="{ item }">
			<span>{{ getItemValue(personRoles,item.role) }}</span>
		</template>

		<template v-slot:[`item.actions`]="{ item }">
			<div class="d-flex">
				<CreatePersonDialog @person-updated="getPersons" :person="item"/>
				<RemoveObjectDialog 
					@remove-object="removePerson(item)" 
					title="Remover Pessoa"
					text="Tem a certeza que quer remover esta pessoa?"
					icon="mdi-account"
				/>
			</div>
		</template>
	</v-data-table>

</template>


<script setup lang="ts">

import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

import RemoteService from '@/services/RemoteService'

import type PersonDto from '@/models/dtos'
import { personRoles } from '@/models/person/PersonRoles'
import { getItemValue, fuzzySearch } from '@/lib/utils'

import CreatePersonDialog from '@/views/persons/CreatePersonDialog.vue'
import AssignReservationDialog from '@/views/reservations/AssignReservationDialog.vue'
import RemoveObjectDialog from '@/components/RemoveObjectDialog.vue'


const search = ref('')
const router = useRouter()

const headers = [
	{ title: 'ID', value: 'id', key: 'id' },
	{ title: 'Nome', value: 'name', key: 'name' },
	{ title: 'Email', value: 'email', key: 'email' },
	{ title: 'Função', value: 'role', key: 'role' },
	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false }
]

const persons: PersonDto[] = reactive([])


getPersons()

async function getPersons() {
	persons.splice(0, persons.length)

	RemoteService.getPersons().then(async (data) => {
		data.forEach((person: PersonDto) => {
			persons.push(person)
		})
	})
}


function removePerson(person) {
	RemoteService.deletePerson(person).then(() => {
		getPersons()
	})
}


</script>
