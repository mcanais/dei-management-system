<template>
	<div>
		<v-dialog v-model="dialog" max-width="400">
			<template v-slot:activator="{ props: activatorProps }">
				<v-icon
					v-bind="activatorProps"
					class="mr-5"
				>mdi-delete</v-icon>
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
	</div>
</template>

<script setup lang="ts">

import { ref } from 'vue'
import RemoteService from '@/services/RemoteService'


const dialog = ref(false)

const emit = defineEmits(['person-removed'])

const props = defineProps({
	person: Object
})

function removePerson() {
	RemoteService.deletePerson(props.person).then(() => {
		emit('person-removed')
	})
}

</script>
