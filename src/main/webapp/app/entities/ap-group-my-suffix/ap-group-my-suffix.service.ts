import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { APGroupMySuffix } from './ap-group-my-suffix.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<APGroupMySuffix>;

@Injectable()
export class APGroupMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/ap-groups';

    constructor(private http: HttpClient) { }

    create(aPGroup: APGroupMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(aPGroup);
        return this.http.post<APGroupMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(aPGroup: APGroupMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(aPGroup);
        return this.http.put<APGroupMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<APGroupMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<APGroupMySuffix[]>> {
        const options = createRequestOption(req);
        return this.http.get<APGroupMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<APGroupMySuffix[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: APGroupMySuffix = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<APGroupMySuffix[]>): HttpResponse<APGroupMySuffix[]> {
        const jsonResponse: APGroupMySuffix[] = res.body;
        const body: APGroupMySuffix[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to APGroupMySuffix.
     */
    private convertItemFromServer(aPGroup: APGroupMySuffix): APGroupMySuffix {
        const copy: APGroupMySuffix = Object.assign({}, aPGroup);
        return copy;
    }

    /**
     * Convert a APGroupMySuffix to a JSON which can be sent to the server.
     */
    private convert(aPGroup: APGroupMySuffix): APGroupMySuffix {
        const copy: APGroupMySuffix = Object.assign({}, aPGroup);
        return copy;
    }
}
