<template>
	<v-app-bar>
		<div class="ist-logo">
			<img src="@/images/ist-logo.svg" class="ist-logo"></img>
		</div>

		<div class="navbar">
			<v-btn 
				v-for="item in navbarItems" 
				:key="item.name" 
				:to="item.path"
				class="nav-button" 
				border="sm"
				rounded="xl"
				:append-icon="item.icon"
				:text="item.name"
			></v-btn>
		</div>

	</v-app-bar>
</template>


<script setup lang="ts">

const navbarItems = [
	{ name: 'Dashboard', path: '/', icon: 'mdi-home'},
	{ name: 'Pessoal', path: '/persons', icon: 'mdi-account-group' },
	{ name: 'Recursos', path: '/resources', icon: 'mdi-briefcase' },
]

</script>

<style scoped>

.ist-logo {
	display: flex;
	justify-content: center;
	margin-left: 5%;
	margin-right: 5%;
}

.navbar {
	display: flex;
	justify-content: center;
	position: absolute;
	left: 50%;
	transform: translateX(-50%);
}

.nav-button {
	margin: 0 15px;
}

.nav-button:hover {
	background-color: rgba(245,245,245 ,0.5);
}

/* FIXME: Make the text on the navigation button disappear
          when the size of the screen is too small*/
@media (max-width: 620px) {
	.nav-button {
	}
}

</style>

