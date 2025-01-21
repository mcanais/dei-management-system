<template>
	<div>
		<v-dialog v-model="dialog" max-width="400">
			<template v-slot:activator="{ props: activatorProps }">
				<!-- Create Person Button !-->
				<v-btn
					v-if="props.person == null"
					class="text-none font-weight-regular"
					prepend-icon="mdi-plus"
					text="Adicionar Pessoa"
					v-bind="activatorProps"
					color="primary"
					rounded="xl"
					@click="initialize"
				/>

				<!-- Edit Person Icon !-->
				<v-icon
					v-else-if="props.buttonText != true"
					v-bind="activatorProps"
					class="mr-2"
					@click="initialize"
				>mdi-pencil</v-icon>

				<!-- Edit Person Button !-->
				<v-btn
					v-else
					v-bind="activatorProps"
					prepend-icon="mdi-pencil"
					text="Editar Pessoa"
					density="comfortable"
					variant="text"
					style="text-transform: none;"
					@click="initialize"
				/>
					
			</template>

			<v-form v-model="valid" @submit.prevent="onSubmit">
				<v-card prepend-icon="mdi-account" :title="title">
					<v-card-text>
						<v-text-field 
							label="IST ID" 
							v-model="newPerson.id"
							v-bind:disabled="props.person != null"
							:rules="istIdRules"
							required 
							class="mb-3"
						></v-text-field>

						<v-text-field 
							label="Nome" 
							v-model="newPerson.name"
							:rules="nameRules"
							required 
							class="mb-3"
						></v-text-field>

						<v-text-field 
							label="Email" 
							v-model="newPerson.email"
							:rules="emailRules"
							required 
							class="mb-3"
						></v-text-field>

						<v-autocomplete
							label="Função"
							v-model="newPerson.role"
							:rules="roleRules"
							:items="Object.values(personRoles)"
							auto-select-first
							required
						></v-autocomplete>
					</v-card-text>

					<v-divider></v-divider>

					<v-card-actions>
						<v-spacer />

						<v-btn 
							text="Cancelar" 
							variant="plain" 
							@click="
							dialog = false;
							resetPerson()
							"
						></v-btn>

						<v-btn
							:text="saveButtonText"
							color="primary"
							:disabled="!valid"
							type="submit"
							@click="
							dialog = false;
							createPerson()
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

import type PersonDto from '@/models/dtos'
import { personRoles } from '@/models/person/PersonRoles'
import { getItemValue, getItemKey } from '@/lib/utils'
import { istIdRules, nameRules, emailRules, roleRules } from '@/models/person/rules'

const dialog = ref(false)
const valid = ref(false)

const emit = defineEmits(['person-updated'])

const newPerson = reactive<PersonDto>({
	id: '',
	name: '',
	email: '',
	role: '',
})

const title = ref('')
const saveButtonText = ref('')

const props = defineProps({
	person: Object || null,
	buttonText: Boolean || null,
})

const createPerson = async () => {
	newPerson.role = getItemKey(personRoles,newPerson.role)

	await RemoteService.createPerson(newPerson)

	resetPerson()

	emit('person-updated')
}

function initialize() {
	if (props.person != null) {
		title.value = 'Editar Pessoa'
		saveButtonText.value = 'Salvar'

		newPerson.id = props.person.id
		newPerson.name = props.person.name
		newPerson.email = props.person.email
		newPerson.role = getItemValue(personRoles, props.person.role)
	} else {
		title.value = 'Nova Pessoa'
		saveButtonText.value = 'Adicionar'
	}
}

function resetPerson() {
	newPerson.id = ''
	newPerson.name = ''
	newPerson.email = ''
	newPerson.role = ''
}

function onSubmit() {
	return !valid
}

</script> 
