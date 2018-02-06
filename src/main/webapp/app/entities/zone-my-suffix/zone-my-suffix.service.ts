import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ZoneMySuffix } from './zone-my-suffix.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<ZoneMySuffix>;

@Injectable()
export class ZoneMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/zones';

    constructor(private http: HttpClient) { }

    create(zone: ZoneMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(zone);
        return this.http.post<ZoneMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(zone: ZoneMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(zone);
        return this.http.put<ZoneMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ZoneMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<ZoneMySuffix[]>> {
        const options = createRequestOption(req);
        return this.http.get<ZoneMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<ZoneMySuffix[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: ZoneMySuffix = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<ZoneMySuffix[]>): HttpResponse<ZoneMySuffix[]> {
        const jsonResponse: ZoneMySuffix[] = res.body;
        const body: ZoneMySuffix[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to ZoneMySuffix.
     */
    private convertItemFromServer(zone: ZoneMySuffix): ZoneMySuffix {
        const copy: ZoneMySuffix = Object.assign({}, zone);
        return copy;
    }

    /**
     * Convert a ZoneMySuffix to a JSON which can be sent to the server.
     */
    private convert(zone: ZoneMySuffix): ZoneMySuffix {
        const copy: ZoneMySuffix = Object.assign({}, zone);
        return copy;
    }
}
