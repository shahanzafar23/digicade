export class Registration {
  constructor(
    public firstName: string,
    public lastName: string,
    public login: string,
    public email: string,
    public password: string,
    public authorities: string[],
    public langKey: string,
    public activated: boolean
  ) {}
}
