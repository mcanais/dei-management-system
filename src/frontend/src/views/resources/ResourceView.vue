<template>
	<v-row align="center" class="mb-2">
		<v-col>
			<h2 class="text-left ml-1">Recursos</h2>
		</v-col>

		<v-col cols="auto">
			<UpdateResourceDialog @resource-updated="getResources" />
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
		<template v-slot:[`item.type`]="{ item }">
			<span>{{ getItemValue(resourceTypes, item.type) }}</span>
		</template>

		<template v-slot:[`item.state`]="{ item }">
			<span>{{ getItemValue(resourceStates, item.state) }}</span>
		</template>

		<template v-slot:[`item.actions`]="{ item }">
			<v-row align="center">
				<UpdateResourceDialog @resource-updated="getResources" :resource="item"/>
				<RemoveResourceDialog @resource-removed="getResources" :resource="item"/>
				<AssignReservationDialog @reservation-assigned="getResources" :resource="item" :maintenance="false"/>
				<AssignReservationDialog @reservation-assigned="getResources" :resource="item" :maintenance="true"/>
				<!--MaintenanceDialog @maintenance-assigned="getResources" :resource="item"/!-->
			</v-row>
		</template>
	</v-data-table>

</template>


<script setup lang="ts">

import { ref, reactive } from 'vue'

import RemoteService from '@/services/RemoteService'

import UpdateResourceDialog from '@/views/resources/UpdateResourceDialog.vue'
//import EditResourceDialog from '@/views/resources/EditResourceDialog.vue'
import RemoveResourceDialog from '@/views/resources/RemoveResourceDialog.vue'
import AssignReservationDialog from '@/views/resources/AssignReservationDialog.vue'
//import MaintenanceDialog from '@/views/resources/MaintenanceDialog.vue'
import type ResourceDto from '@/models/dtos'
import { resourceTypes } from '@/models/resource/ResourceTypes'
import { resourceStates } from '@/models/resource/ResourceStates'
import { getItemValue, fuzzySearch } from '@/lib/utils'


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

</script>
