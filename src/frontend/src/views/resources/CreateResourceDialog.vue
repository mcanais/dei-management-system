<template>
	<div>
		<v-dialog v-model="dialog" max-width="400">
			<template v-slot:activator="{ props: activatorProps }">
				<!-- Create Resource Button !-->
				<v-btn
					v-if="props.resource == null"
					class="class text-none font-weight-regular"
					prepend-icon="mdi-plus"
					text="Adicionar Recurso"
					v-bind="activatorProps"
					color="primary"
					rounded="xl"
					@click="initialize"
				/>

				<!-- Edit Resource Icon !-->
				<v-btn
					v-else-if="props.buttonText != true"
					v-bind="activatorProps"
					class="mr-2 pa-1"
					density="comfortable"
					variant="outlined"
					size="small"
					color="primary"
					icon="mdi-pencil"
					@click="initialize"
				>
					<v-icon/>
					<v-tooltip
						activator="parent"
						location="bottom"
						offset="5"
						text="Editar Pessoa"
					/>
				</v-btn>

				<!-- Edit Resource Text Button !-->
				<v-btn
					v-else
					v-bind="activatorProps"
					prepend-icon="mdi-pencil"
					text="Editar Recurso"
					density="comfortable"
					variant="text"
					style="text-transform: none;"
					@click="initialize"
				/>
			</template>

			<v-form v-model="valid" @submit.prevent="onSubmit">
				<v-card prepend-icon="mdi-briefcase" :title="title">
					<v-card-text>
						<v-text-field 
							label="Nome" 
							v-model="newResource.name"
							:rules="nameRules"
							required 
							class="mb-3"
						></v-text-field>

						<v-autocomplete
							label="Tipo"
							v-model="newResource.type"
							:rules="typeRules"
							:items="Object.values(resourceTypes)"
							auto-select-first
							required
							class="mb-3"
						></v-autocomplete>

						<v-text-field 
							label="Localização" 
							v-model="newResource.location"
							:rules="locationRules"
							required 
						></v-text-field>
					</v-card-text>

					<v-divider></v-divider>

					<v-card-actions>
						<v-spacer></v-spacer>

						<v-btn 
							text="Cancelar" 
							variant="plain" 
							@click="
							dialog = false;
							resetResource()
							"
						></v-btn>

						<v-btn
							:text="saveButtonText"
							color="primary"
							:disabled="!valid"
							type="submit"
							@click="
							dialog = false;
							createResource()
							"
						></v-btn>
					</v-card-actions>
				</v-card>
			</v-form>
		</v-dialog>
	</div>
</template>

<script setup lang="ts">

import { ref, reactive } from 'vue'

import RemoteService from '@/services/RemoteService'

import type ResourceDto from '@/models/dtos'
import { resourceTypes } from '@/models/resource/ResourceTypes'
import { getItemValue, getItemKey } from '@/lib/utils'
import { nameRules, typeRules, locationRules } from '@/models/resource/rules'

const dialog = ref(false)
const valid = ref(false)

const emit = defineEmits(['resource-updated'])

const newResource = reactive<ResourceDto>({
	id: '',
	name: '',
	type: '',
	state: '',
	location: '',
})

const title = ref('')
const saveButtonText = ref('')

const props = defineProps({
	resource: Object || null,
	buttonText: Boolean || null,
})

const createResource = async () => {
	newResource.type = getItemKey(resourceTypes,newResource.type)

	if (props.resource == null) {
		newResource.state = 'AVAILABLE'
		await RemoteService.createResource(newResource)
	} else {
		await RemoteService.updateResource(newResource)
	}

	resetResource()

	emit('resource-updated')
}

function initialize() {
	if (props.resource != null) {
		title.value = 'Editar Recurso'
		saveButtonText.value = 'Salvar'

		newResource.id = props.resource.id
		newResource.name = props.resource.name
		newResource.type = getItemValue(resourceTypes, props.resource.type)
		newResource.state = props.resource.state
		newResource.location = props.resource.location
	} else {
		title.value = 'Novo Recurso'
		saveButtonText.value = 'Adicionar'

		resetResource()
	}
}

function resetResource() {
	newResource.id = ''
	newResource.name = ''
	newResource.type = ''
	newResource.state = ''
	newResource.location = ''
}

function onSubmit() {
	return !valid
}

</script> 
