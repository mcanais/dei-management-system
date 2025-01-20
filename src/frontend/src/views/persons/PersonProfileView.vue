<template>
	
	<h2 class="text-left ml-1 mb-3">Informações Pessoa</h2>
	<h2 class="text-left ml-1 mb-3">{{ tempPerson.id }}</h2>

	<div class="profile-container">
		<div class="person-info">
			<ul>
				<li><strong>IST Id:</strong> {{ tempPerson.id }}</li>
				<li><strong>Nome:</strong> {{ tempPerson.name }}</li>
				<li><strong>Email:</strong> {{ tempPerson.email }}</li>
			</ul>
		</div>

		<div class="person-reservations">
			<h2>Descrição</h2>
			<p>descricao aaaaa</p>
		</div>
	</div>

</template>


<script setup lang="ts">

import { ref, reactive } from 'vue'

import RemoteService from '@/services/RemoteService'

import type PersonDto from '@/models/dtos'
import { personRoles } from '@/models/person/PersonRoles'
import { getItemValue, fuzzySearch } from '@/lib/utils'


const search = ref('')

const props = defineProps({
	personId: String
})

const tempPerson = reactive<PersonDto>({
	id: '',
	name: '',
	email: '',
	role: '',
})

//const headers = [
//	{ title: 'ID', value: 'id', key: 'id' },
//	{ title: 'Nome', value: 'name', key: 'name' },
//	{ title: 'Email', value: 'email', key: 'email' },
//	{ title: 'Função', value: 'role', key: 'role' },
//	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false }
//]


getPersonData()

async function getPersonData() {
	RemoteService.getPerson(props.personId).then(async (data) => {
		tempPerson.id = data.id
		tempPerson.name = data.name
		tempPerson.email = data.email
	})
}

</script>

<style scoped>

.profile-container {
	display: flex;
	justify-content: space-between;
	gap: 15px;
}

.person-info {
	flex-grow: 1;
	background-color: #f4f4f4;
	padding: 20px;
	border-radius: 8px;
}

.person-reservations {
	flex-grow: 1;
	background-color: #fff;
	padding: 20px;
	border-radius: 8px;
	border: 1px solid #ddd;
}

</style>
