<template>
	<v-chip 
		v-if="props.state != ''"
		:text="states[props.state].text"
		:color="states[props.state].color"
		:append-icon="states[props.state].icon"
		class="text-white"
		variant="tonal"
	/>
</template>

<script setup lang="ts">

const props = defineProps({
	state: {
		type: String,
		default: '',
	}
})

const states = {
	'AVAILABLE': {
		color: 'green',
		text: 'Disponível',
		icon: ''
	},
	'INUSE': {
		color: 'primary',
		text: 'Em Uso',
		icon: 'mdi-account'
	},
	'MAINTENANCE': {
		color: 'orange',
		text: 'Em Manutenção',
		icon: 'mdi-cog'
	},
}

</script>
