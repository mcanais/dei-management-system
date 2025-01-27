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


// Custom sort for dates
// Returns:
//    1 if date A comes after date B
//   -1 if date B comes after date A
//    0 if date A is the same as date B
export function sortByDate(a: string, b: string): number {
	const dateA = getDateFromString(a)
	const dateB = getDateFromString(b)

	if (dateA > dateB) {
		return 1
	} else if (dateB > dateA) {
		return -1
	} else {
		return 0
	}
}
