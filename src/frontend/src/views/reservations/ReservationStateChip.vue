<template>
	<v-chip 
		:text="states[props.state].text"
		:color="states[props.state].color"
		variant="tonal"
	/>
</template>

<script setup lang="ts">

const props = defineProps({
	state: String
})

const states = {
	'ACTIVE': {
		color: 'green',
		text: 'Ativo',
	},
	'PENDING': {
		color: 'orange',
		text: 'Pendente',
	},
	'FINISHED': {
		color: 'purple',
		text: 'Finalizado',
	},
	'CANCELLED': {
		color: 'red',
		text: 'Cancelado',
	},
}

</script>
