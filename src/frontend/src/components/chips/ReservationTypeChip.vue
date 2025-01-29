<template>
	<v-chip
		v-if="props.type == 'MAINTENANCE'"
		text="Manutenção"
		color="orange"
		append-icon="mdi-cog"
		variant="outlined"
	/>
	<v-chip
		v-else-if="props.type == 'PERSON'"
		:text="personText"
		color="primary"
		append-icon="mdi-account"
		variant="outlined"	
		@mouseenter="personText = props.assignedPersonId"
		@mouseleave="personText = 'Reserva'"
		@click="router.push(`/persons/${props.assignedPersonId}`)"
	/>
</template>

<script setup lang="ts">

import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const props = defineProps({
	type: String,
	assignedPersonId: String
})

const personText = ref('Reserva')


</script>
