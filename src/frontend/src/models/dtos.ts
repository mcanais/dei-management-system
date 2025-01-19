export class PersonDto {
    id?: string
    name?: string
    email?: string
    role?: string
	reservations?: ReservationDto[]
  
	constructor(jsonObj: Partial<PersonDto>) {
		Object.assign(this, jsonObj)
	}
}

export class ResourceDto {
    id?: string
    name?: string
    type?: string
    state?: string
    location?: string
	reservations?: ReservationDto[]
  
	constructor(jsonObj: Partial<ResourceDto>) {
		Object.assign(this, jsonObj)
	}
}

export class ReservationDto {
    id?: string
    startDate?: string
    finishDate?: string
	type?: string
	state?: string
    assignedResourceId?: string
  
	constructor(jsonObj: Partial<ReservationDto>) {
		Object.assign(this, jsonObj)
	}
}
