<template>
	<v-dialog v-model="dialog" max-width="400">
		<template v-slot:activator="{ props: activatorProps }">
			<!-- Remove Object Icon !-->
			<v-btn
				v-if="props.buttonText != true"
				v-bind="activatorProps"
				class="mr-2 pa-1"
				density="comfortable"
				variant="outlined"
				size="small"
				color="error"
				icon="mdi-delete"
			/>

			<!-- Remove Object Text Button !-->
			<v-btn
				v-else
				v-bind="activatorProps"
				prepend-icon="mdi-delete"
				color="error"
				:text="props.title"
				density="comfortable"
				variant="text"
				style="text-transform: none;"
			/>
		</template>

		<v-card :prepend-icon="props.icon" :title="props.title">
			<v-card-text>{{ props.text }}</v-card-text>

			<v-divider></v-divider>

			<v-card-actions>
				<v-spacer></v-spacer>

				<v-btn 
					:text="props.cancelText"
					variant="plain" 
					@click="
					dialog = false;
					"
				></v-btn>

				<v-btn
					:text="props.submitText"
					color="error"
					@click="
					dialog = false;
					emit('remove-object')
					"
				></v-btn>
			</v-card-actions>
		</v-card>
	</v-dialog>
</template>

<script setup lang="ts">

import { ref } from 'vue'

const dialog = ref(false)

const emit = defineEmits(['remove-object'])

const props = defineProps({
	buttonText: Boolean,
	title: String,
	text: String,
	icon: String,
	submitText: {
		type: String,
		default: "Remover"
	},
	cancelText: {
		type: String,
		default: "Cancelar"
	},
})

</script>
