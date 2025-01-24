<template>
	<v-dialog v-model="dialog" max-width="400">
		<template v-slot:activator="{ props: activatorProps }">
			<!-- Remove Person Icon !-->
			<v-icon
				v-if="props.buttonText != true"
				v-bind="activatorProps"
				class="mr-5"
			>mdi-delete</v-icon>

			<!-- Remove Person Button !-->
			<v-btn
				v-else
				v-bind="activatorProps"
				prepend-icon="mdi-delete"
				color="error"
				text="Remover Pessoa"
				density="comfortable"
				variant="text"
				style="text-transform: none;"
			/>
		</template>

		<v-card prepend-icon="mdi-account" title="Remover Pessoa">
			<v-card-text>Tem a certeza que quer remover esta pessoa?</v-card-text>

			<v-divider></v-divider>

			<v-card-actions>
				<v-spacer></v-spacer>

				<v-btn 
					text="Cancelar" 
					variant="plain" 
					@click="
					dialog = false;
					"
				></v-btn>

				<v-btn
					text="Remover"
					color="error"
					@click="
					dialog = false;
					removePerson()
					"
				></v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script setup lang="ts">

import { ref } from 'vue'
import { useRouter } from 'vue-router'
import RemoteService from '@/services/RemoteService'

const router = useRouter()


const dialog = ref(false)

const emit = defineEmits(['person-removed'])

const props = defineProps({
	person: Object,
	buttonText: Boolean || null,
})

function removePerson() {
	RemoteService.deletePerson(props.person).then(() => {
		emit('person-removed')
		router.push(`/persons`)
	})
}

</script>
