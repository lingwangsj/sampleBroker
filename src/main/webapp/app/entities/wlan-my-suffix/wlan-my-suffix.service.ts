import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { WlanMySuffix } from './wlan-my-suffix.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<WlanMySuffix>;

@Injectable()
export class WlanMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/wlans';

    constructor(private http: HttpClient) { }

    create(wlan: WlanMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(wlan);
        return this.http.post<WlanMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(wlan: WlanMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(wlan);
        return this.http.put<WlanMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<WlanMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<WlanMySuffix[]>> {
        const options = createRequestOption(req);
        return this.http.get<WlanMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<WlanMySuffix[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: WlanMySuffix = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<WlanMySuffix[]>): HttpResponse<WlanMySuffix[]> {
        const jsonResponse: WlanMySuffix[] = res.body;
        const body: WlanMySuffix[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to WlanMySuffix.
     */
    private convertItemFromServer(wlan: WlanMySuffix): WlanMySuffix {
        const copy: WlanMySuffix = Object.assign({}, wlan);
        return copy;
    }

    /**
     * Convert a WlanMySuffix to a JSON which can be sent to the server.
     */
    private convert(wlan: WlanMySuffix): WlanMySuffix {
        const copy: WlanMySuffix = Object.assign({}, wlan);
        return copy;
    }
}
