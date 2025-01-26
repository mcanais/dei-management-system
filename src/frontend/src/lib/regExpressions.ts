// Verifies if a string only contains numbers
export function onlyNumbers(v: string): boolean {
	return /^\d+$/.test(v);
}

// Verifies if a string only contains letters or spaces
export function onlyLetters(v: string): boolean {
	return /^[A-Za-z\s]+$/.test(v);
}

// Verifies if a string is a valid email
export function validEmail(v: string): boolean {
	return /.+@.+\..+/.test(v);
}

// Verifies if a string is a valid date
// Follows the format dd/mm/yyyy
export function validDate(v: string): boolean {
	return /^([0-2][0-9]|3[01])\/(0[1-9]|1[0-2])\/([0-9]{4})$/.test(v);
}
