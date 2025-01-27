<template>
	<v-chip 
		:text="types[props.type].text"
		:color="types[props.type].color"
		:append-icon="types[props.type].icon"
		variant="outlined"
	/>
</template>

<script setup lang="ts">

const props = defineProps({
	type: String
})

const types = {
	'PERSON': {
		color: 'primary',
		text: 'Reserva',
		icon: 'mdi-account',
	},
	'MAINTENANCE': {
		color: 'orange',
		text: 'Manutenção',
		icon: 'mdi-cog',
	},
}

</script>
