<template>
	<div>
		<v-dialog v-model="dialog" max-width="400">
			<template v-slot:activator="{ props: activatorProps }">
				<v-icon
					v-bind="activatorProps"
					class="mr-2"
				>mdi-delete</v-icon>
			</template>

			<v-card prepend-icon="mdi-briefcase" title="Remover Recurso">
				<v-card-text>Tem a certeza que quer remover este recurso?</v-card-text>

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
						removeResource()
						"
					></v-btn>
				</v-card-actions>
			</v-card>
		</v-dialog>
	</div>
</template>

<script setup lang="ts">

import { ref } from 'vue'
import RemoteService from '@/services/RemoteService'


const dialog = ref(false)

const emit = defineEmits(['resource-removed'])

const props = defineProps({
	resource: Object
})

function removeResource() {
	RemoteService.deleteResource(props.resource).then(() => {
		emit('resource-removed')
	})
}

</script>
