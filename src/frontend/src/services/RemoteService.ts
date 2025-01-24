import axios from 'axios'

import {PersonDto, ResourceDto, ReservationDto} from '@/models/dtos'

const httpClient = axios.create()
httpClient.defaults.timeout = 10000
httpClient.defaults.baseURL = import.meta.env.VITE_ROOT_API
httpClient.defaults.headers.post['Content-Type'] = 'application/json'

export default class RemoteService {
	// ------------------- Persons -------------------
	static async getPersons(): Promise<PersonDto[]> {
		return httpClient.get('/persons/all').then((response) => {
			return response.data.map((person: any) => {
				return new PersonDto(person)
			})
		})
	}

	static async getPerson(personId: string): Promise<PersonDto> {
		return httpClient.get(`/persons/${personId}`).then((response) => {
			return new PersonDto(response.data)
		})
	}

	static async createPerson(person: PersonDto): Promise<PersonDto> {
		return httpClient.post('/persons/create', person).then((response) => {
			return new PersonDto(response.data)
		})
	}

	static async updatePerson(person: PersonDto): Promise<PersonDto> {
		return httpClient.put('/persons/update', person).then((response) => {
			return new PersonDto(response.data)
		})
	}

	static async deletePerson(person: PersonDto): Promise<void> {
		return httpClient.delete(`/persons/delete/${person.id}`)
	}


	// ------------------- Resources -------------------
	static async getResources(): Promise<ResourceDto[]> {
		return httpClient.get('/resources/all').then((response) => {
			return response.data.map((resource: any) => {
				return new ResourceDto(resource)
			})
		})
	}

	static async createResource(resource: ResourceDto): Promise<ResourceDto> {
		return httpClient.post('/resources/create', resource).then((response) => {
			return new ResourceDto(response.data)
		})
	}

	static async updateResource(resource: ResourceDto): Promise<ResourceDto> {
		return httpClient.put('/resources/update', resource).then((response) => {
			return new ResourceDto(response.data)
		})
	}

	static async deleteResource(resource: ResourceDto): Promise<void> {
		return httpClient.delete(`/resources/delete/${resource.id}`)
	}
	
	// ------------------- Reservations -------------------
	static async assignReservation(reservationDto: ReservationDto, personId: string, resourceId: string) {
		return httpClient.put(`/reservations/assign/${personId}/${resourceId}`, reservationDto).then((response) => {
			return new ReservationDto(response.data)
		})
	}

	static async assignMaintenance(reservationDto: ReservationDto, resourceId: string) {
		return httpClient.put(`/reservations/maintenance/${resourceId}`, reservationDto).then((response) => {
			return new ReservationDto(response.data)
		})
	}

	static async cancelReservation(reservation: ReservationDto): Promise<ReservationDto> {
		return httpClient.put('/reservations/cancel', reservation).then((response) => {
			return new ReservationDto(response.data)
		})
	}
}
