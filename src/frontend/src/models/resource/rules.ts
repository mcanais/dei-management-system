export const nameRules = [
	(v: string) => !!v || 'Insira o nome',
	(v: string) => (v.length > 0 && v.length <= 40) || 'Nome inválido',
];

export const typeRules = [
	(v: string) => !!v || 'Insira o tipo',
];

export const stateRules = [
	(v: string) => !!v || 'Insira o estado',
];

export const locationRules = [
	(v: string) => (v.length <= 40) || 'Localização inválida',
];
