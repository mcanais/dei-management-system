import { onlyNumbers, onlyLetters, validEmail } from '@/lib/regExpressions'

export const istIdRules = [
	(v: string) => !!v || 'Insira o IST ID',
	(v: string) => (v.length >= 5 && v.length <= 10) || 'IST ID inválido',
    (v: string) => (v.startsWith('ist')) || 'IST ID inválido',
    (v: string) => (onlyNumbers(v.substring(3))) || 'IST ID inválido',
];

export const nameRules = [
	(v: string) => !!v || 'Insira o nome',
	(v: string) => (v.length >= 3 && v.length <= 30) || 'Nome inválido',
	(v: string) => (onlyLetters(v)) || 'Nome inválido',
];

export const emailRules = [
	(v: string) => !!v || 'Insira o email',
	(v: string) => validEmail(v) || 'Email inválido',
];

export const roleRules = [
	(v: string) => !!v || 'Insira a função',
];
