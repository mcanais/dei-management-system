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


// Gets the date that is n days away from today's date
// 		getRelativeDate(0) --> today's date
//		getRelativeDate(1) --> tomorrow's date
//		getRelativeDate(-1) --> yesterday's date
export function getRelativeDate(n: number): Date {
	const todayDate = new Date()
	todayDate.setDate(todayDate.getDate() + n)

	return todayDate
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
