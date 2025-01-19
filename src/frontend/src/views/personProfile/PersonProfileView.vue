<template>

	<h2> hello </h2>
	


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
		console.log(data)
	})
}



</script>
