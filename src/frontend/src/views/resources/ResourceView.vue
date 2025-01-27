<template>
	<v-row align="center" class="mb-2">
		<v-col>
			<h2 class="text-left ml-1">Recursos</h2>
		</v-col>

		<v-col cols="auto">
			<CreateResourceDialog @resource-updated="getResources" />
		</v-col>
	</v-row>

	<v-text-field
		v-model="search"
		label="Procurar"
		prepend-inner-icon="mdi-magnify"
		variant="outlined"
		hide-details
		single-line
	>
	</v-text-field>
	
	<v-data-table
		:headers="headers"
		:items="resources"
		:search="search"
		:custom-filter="fuzzySearch"
		no-data-text="Ainda não existem recursos na base de dados"
		class="text-left"
	>
		<template v-slot:[`item.id`]="{ item }">
			<button 
				class="id-button" 
				@click="router.push(`/resources/${item.id}`)"
			>{{ item.id }}</button>
		</template>

		<template v-slot:[`item.type`]="{ item }">
			<span>{{ getItemValue(resourceTypes, item.type) }}</span>
		</template>

		<template v-slot:[`item.state`]="{ item }">
			<ResourceStateChip :state="item.state"/>
		</template>

		<template v-slot:[`item.actions`]="{ item }">
			<div class="d-flex">
				<CreateResourceDialog @resource-updated="getResources" :resource="item"/>
				<RemoveObjectDialog @remove-object="removeResource(item)" 
					title="Remover Recurso"
					text="Tem a certeza que quer remover este recurso?"
					icon="mdi-briefcase"
				/>
				<AssignReservationDialog @reservation-assigned="getResources" :resource="item" mode="reservation"/>
				<AssignReservationDialog @reservation-assigned="getResources" :resource="item" mode="maintenance"/>
			</div>
		</template>
	</v-data-table>

</template>


<script setup lang="ts">

import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

import RemoteService from '@/services/RemoteService'

import type ResourceDto from '@/models/dtos'

import CreateResourceDialog from '@/views/resources/CreateResourceDialog.vue'
import RemoveObjectDialog from '@/components/RemoveObjectDialog.vue'
import AssignReservationDialog from '@/views/reservations/AssignReservationDialog.vue'
import ResourceStateChip from '@/components/chips/ResourceStateChip.vue'

import { resourceTypes } from '@/models/resource/ResourceTypes'
import { resourceStates } from '@/models/resource/ResourceStates'
import { getItemValue, fuzzySearch } from '@/lib/utils'


const router = useRouter()
const search = ref('')

const headers = [
	{ title: 'ID', value: 'id', key: 'id' },
	{ title: 'Nome', value: 'name', key: 'name' },
	{ title: 'Tipo', value: 'type', key: 'type' },
	{ title: 'Estado', value: 'state', key: 'state' },
	{ title: 'Localização', value: 'location', key: 'location' },
	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false}
]

const resources: ResourceDto[] = reactive([])


getResources()

async function getResources() {
	resources.splice(0, resources.length) // Remove everything from resources

	RemoteService.getResources().then(async (data) => {
		data.forEach((resource: ResourceDto) => {
			resources.push(resource)
		})
	})
}

function removeResource(resource) {
	RemoteService.deleteResource(resource).then(() => {
		getResources()
	})
}

</script>
