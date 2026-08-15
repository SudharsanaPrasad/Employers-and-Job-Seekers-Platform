export type Role = 'JOB_SEEKER' | 'EMPLOYER'

export type ApplicationStatus = 'APPLIED' | 'REVIEWED' | 'ACCEPTED' | 'REJECTED'

export interface AuthUser {
  userId: string
  name: string
  email: string
  role: Role
}

export interface AuthResponse extends AuthUser {
  token: string
}

export interface Job {
  id: string
  title: string
  description: string
  location: string
  salary: string
  deadline: string
  employerId: string
  employerName: string
  createdAt: string
}

export interface Application {
  id: string
  jobId: string
  jobTitle: string
  seekerId: string
  seekerName: string
  employerId: string
  status: ApplicationStatus
  coverLetter?: string
  appliedAt: string
}

export interface Page<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}
