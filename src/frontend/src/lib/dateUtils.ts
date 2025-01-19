import { validDate } from '@/lib/regExpressions'

// Convert a date to a string dd/mm/yyyy
export function getStringFromDate(d: Date) {
	const day = String(d.getDate()).padStart(2, '0')
	const month = String(d.getMonth() + 1).padStart(2, '0')
	const year = d.getFullYear()

	return `${day}/${month}/${year}`
}


// Convert a string dd/mm/yyyy to the corresponding date
export function getDateFromString(s: string) {
	if (!validDate(s)) {
		return null;
	}

	const [day, month, year] = s.split('/')

	return new Date(year, month-1, day)
}


// Gets the current day as a string dd/mm/yyyy
export function getCurrentDay(): string {
	const currentDay = new Date()

	return getStringFromDate(currentDay)
}


// Gets the day of tomorrow as a string dd/mm/yyyy
export function getTomorrowDay(): string {
	const tomorrow = new Date()
	tomorrow.setDate(tomorrow.getDate() + 1)

	return getStringFromDate(tomorrow)
}


// Verifies if the date of s1 comes before the date of s2
export function isPastDate(s1, s2): bool {
	const date = getDateFromString(s1)
	const date2 = getDateFromString(s2)

	return date < date2
}


// Verifies if the date of s1 comes after the date of s2
export function isFutureDate(s1, s2): bool {
	const date = getDateFromString(s1)
	const date2 = getDateFromString(s2)

	return date > date2
}


// Verifies if the new reservation overlaps with the existing reservations
export function overlappingReservations(reservations, newReservation) {
	for (const reservation of reservations) {
		if (isPastDate(newReservation.startDate, reservation.finishDate) &&
				isFutureDate(newReservation.finishDate,reservation.startDate)) {
			return true
		}
	}

	return false
}
