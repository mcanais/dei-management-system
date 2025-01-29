<template>
  <v-chart class="chart" :option="options" autoresize />
</template>

<script setup lang="ts">

import { ref } from "vue";

import * as echarts from "echarts/core";
import { CanvasRenderer } from "echarts/renderers";
import { BarChart } from "echarts/charts";
import { GridComponent } from "echarts/components";
import VChart from "vue-echarts";

echarts.use([
	CanvasRenderer,
	BarChart,
	GridComponent,
])


const options = ref({
	backgroundColor: 'transparent',
	tooltip: {
		trigger: 'axis',
		axisPointer: {
			type: 'shadow',
		},
	},
	xAxis: { 
		type: 'category',
		data: ['Mon','Tues','Wedn','Thurs', 'Something']
	},
	yAxis: {
		type: 'value',
	},
	series: [
		{
			data: [120,200,50,250],
			type: 'bar'
		}
	]
})

</script>


<style scoped>

.chart {
	height: 300px;
}

</style>
