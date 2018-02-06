import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { APMySuffix } from './ap-my-suffix.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<APMySuffix>;

@Injectable()
export class APMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/aps';

    constructor(private http: HttpClient) { }

    create(aP: APMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(aP);
        return this.http.post<APMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(aP: APMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(aP);
        return this.http.put<APMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<APMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<APMySuffix[]>> {
        const options = createRequestOption(req);
        return this.http.get<APMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<APMySuffix[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: APMySuffix = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<APMySuffix[]>): HttpResponse<APMySuffix[]> {
        const jsonResponse: APMySuffix[] = res.body;
        const body: APMySuffix[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to APMySuffix.
     */
    private convertItemFromServer(aP: APMySuffix): APMySuffix {
        const copy: APMySuffix = Object.assign({}, aP);
        return copy;
    }

    /**
     * Convert a APMySuffix to a JSON which can be sent to the server.
     */
    private convert(aP: APMySuffix): APMySuffix {
        const copy: APMySuffix = Object.assign({}, aP);
        return copy;
    }
}
