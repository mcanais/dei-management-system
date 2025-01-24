<template>
	<v-chip 
		v-if="state=='ACTIVE'"
		color="green"
		variant="tonal"
		text="Ativo"
	/>
	<v-chip 
		v-else-if="state=='PENDING'"
		color="orange"
		variant="tonal"
		text="Pendente"
	/>
	<v-chip 
		v-else-if="state=='FINISHED'"
		color="purple"
		variant="tonal"
		text="Finalizado"
	/>
	<v-chip 
		v-else-if="state=='CANCELLED'"
		color="red"
		variant="tonal"
		text="Cancelado"
	/>
</template>

<script setup lang="ts">

const props = defineProps({
	state: String
})

</script>
