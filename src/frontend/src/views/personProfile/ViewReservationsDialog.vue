<template>
	<div>
		<v-dialog v-model="dialog" max-width="600">
			<template v-slot:activator="{ props: activatorProps }">
				<v-icon
					v-bind="activatorProps"
					class="mr-5"
					@click="initialize"
				>mdi-eye</v-icon>
			</template>

			<v-card prepend-icon="mdi-account" :title="title">
				<v-card-text>
					<v-data-table
						:headers="headers"
						:items="props.user.reservations"
						:hide-default-footer="true"
						class="text-left"
					>
					<template v-slot:[`item.actions`]="{ item }">
						<v-icon @click="removeReservation(item)">mdi-delete</v-icon>
					</template>
					</v-data-table>
				</v-card-text>

				<v-divider></v-divider>

				<v-card-actions>
					<v-btn 
						text="Fechar" 
						variant="plain" 
						@click="
						dialog = false;
						"
					></v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>
	</div>
</template>

<script setup lang="ts">

import { ref, reactive } from 'vue'
import RemoteService from '@/services/RemoteService'

import type UserDto from '@/models/dtos'
import type ResourceDto from '@/models/dtos'
import { userRoles } from '@/models/users/UserRoles'


const dialog = ref(false)
const title = ref('')

const headers = [
	{ title: 'Id Recurso', value: 'assignedResourceId', key: 'resourceName' },
	{ title: 'Data Início', value: 'startDate', key: 'startDate' },
	{ title: 'Data Fim', value: 'finishDate', key: 'finishDate' },
	{ title: 'Ações', value: 'actions', key: 'actions', sortable: false }
]


const emit = defineEmits(['user-update'])

const props = defineProps({
	user: Object
})

function removeReservation(reservation) {
	RemoteService.deleteReservation(reservation).then(() => {
		emit('user-update')
	})
}

function initialize() {
	title.value = 'Reservas de ' + props.user.id
}


</script>
