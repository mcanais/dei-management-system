export function getItemValue(rec: Record<string,string>, key: string): string {
	return rec[key];
}

export function getItemKey(rec: Record<string,string>, value: string): string {
	for (const [key, v] of Object.entries(rec)) {
		if (v === value) {
			return key;
		}
	}
}

export function fuzzySearch(value: string, search: string) {
	// Regex to match any character in between the search characters
	let searchRegex = new RegExp(search.split('').join('.*'), 'i')
	return searchRegex.test(value)
}



