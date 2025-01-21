<template>
	<v-row align="center" class="mb-2">
		<v-col>
			<h2 class="text-left ml-1">Pessoal</h2>
		</v-col>

		<v-col cols="auto">
			<UpdatePersonDialog @person-updated="getPersons" />
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
		class="text-left"
	>
		<template v-slot:[`item.id`]="{ item }">
			<button 
				class="id-button" 
				@click="toPersonProfilePath(item.id)"
			>{{ item.id }}</button>
		</template>

		<template v-slot:[`item.role`]="{ item }">
			<span>{{ getItemValue(personRoles,item.role) }}</span>
		</template>

		<template v-slot:[`item.actions`]="{ item }">
			<v-row align="center">
				<UpdatePersonDialog @person-updated="getPersons" :person="item"/>
				<RemovePersonDialog @person-removed="getPersons" :person="item"/>
			</v-row>
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

import UpdatePersonDialog from '@/views/persons/UpdatePersonDialog.vue'
import RemovePersonDialog from '@/views/persons/RemovePersonDialog.vue'


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
	persons.splice(0, persons.length) // Remove everything from persons

	RemoteService.getPersons().then(async (data) => {
		data.forEach((person: PersonDto) => {
			persons.push(person)
		})
	})
}

function toPersonProfilePath(personId) {
	router.push(`/persons/${personId}`)
}


</script>

<style scoped>

.id-button {
	text-decoration: underline;
	color: #2196F3;
	transition: color 0.2s ease;
}

.id-button:hover {
	color: #215cf3;
}

</style>
