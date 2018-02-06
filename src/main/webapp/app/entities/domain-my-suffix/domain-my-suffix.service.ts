import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { DomainMySuffix } from './domain-my-suffix.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<DomainMySuffix>;

@Injectable()
export class DomainMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/domains';

    constructor(private http: HttpClient) { }

    create(domain: DomainMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(domain);
        return this.http.post<DomainMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(domain: DomainMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(domain);
        return this.http.put<DomainMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<DomainMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<DomainMySuffix[]>> {
        const options = createRequestOption(req);
        return this.http.get<DomainMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<DomainMySuffix[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: DomainMySuffix = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<DomainMySuffix[]>): HttpResponse<DomainMySuffix[]> {
        const jsonResponse: DomainMySuffix[] = res.body;
        const body: DomainMySuffix[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to DomainMySuffix.
     */
    private convertItemFromServer(domain: DomainMySuffix): DomainMySuffix {
        const copy: DomainMySuffix = Object.assign({}, domain);
        return copy;
    }

    /**
     * Convert a DomainMySuffix to a JSON which can be sent to the server.
     */
    private convert(domain: DomainMySuffix): DomainMySuffix {
        const copy: DomainMySuffix = Object.assign({}, domain);
        return copy;
    }
}
