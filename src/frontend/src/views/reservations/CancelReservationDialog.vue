<template>
	<v-dialog v-model="dialog" max-width="400">
		<template v-slot:activator="{ props: activatorProps }">
			<v-icon
				v-bind="activatorProps"
				class="mr-5"
			>mdi-delete</v-icon>
		</template>

		<v-card prepend-icon="mdi-calendar" title="Cancelar Reserva">
			<v-card-text>Tem a certeza que quer cancelar esta reserva?</v-card-text>

			<v-divider></v-divider>

			<v-card-actions>
				<v-spacer></v-spacer>

				<v-btn 
					text="Não" 
					variant="plain" 
					@click="
					dialog = false;
					"
				></v-btn>

				<v-btn
					text="Sim"
					color="error"
					@click="
					dialog = false;
					cancelReservation()
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


const dialog = ref(false)
const emit = defineEmits(['reservation-cancelled'])

const props = defineProps({
	reservation: Object,
})

async function cancelReservation() {
	RemoteService.cancelReservation(props.reservation).then(async (data) => {
		emit('reservation-cancelled')
	})
}

</script>
