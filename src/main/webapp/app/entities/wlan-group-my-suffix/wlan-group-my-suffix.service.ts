import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { WlanGroupMySuffix } from './wlan-group-my-suffix.model';
import { createRequestOption } from '../../shared';

export type EntityResponseType = HttpResponse<WlanGroupMySuffix>;

@Injectable()
export class WlanGroupMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/wlan-groups';

    constructor(private http: HttpClient) { }

    create(wlanGroup: WlanGroupMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(wlanGroup);
        return this.http.post<WlanGroupMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    update(wlanGroup: WlanGroupMySuffix): Observable<EntityResponseType> {
        const copy = this.convert(wlanGroup);
        return this.http.put<WlanGroupMySuffix>(this.resourceUrl, copy, { observe: 'response' })
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<WlanGroupMySuffix>(`${this.resourceUrl}/${id}`, { observe: 'response'})
            .map((res: EntityResponseType) => this.convertResponse(res));
    }

    query(req?: any): Observable<HttpResponse<WlanGroupMySuffix[]>> {
        const options = createRequestOption(req);
        return this.http.get<WlanGroupMySuffix[]>(this.resourceUrl, { params: options, observe: 'response' })
            .map((res: HttpResponse<WlanGroupMySuffix[]>) => this.convertArrayResponse(res));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response'});
    }

    private convertResponse(res: EntityResponseType): EntityResponseType {
        const body: WlanGroupMySuffix = this.convertItemFromServer(res.body);
        return res.clone({body});
    }

    private convertArrayResponse(res: HttpResponse<WlanGroupMySuffix[]>): HttpResponse<WlanGroupMySuffix[]> {
        const jsonResponse: WlanGroupMySuffix[] = res.body;
        const body: WlanGroupMySuffix[] = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            body.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return res.clone({body});
    }

    /**
     * Convert a returned JSON object to WlanGroupMySuffix.
     */
    private convertItemFromServer(wlanGroup: WlanGroupMySuffix): WlanGroupMySuffix {
        const copy: WlanGroupMySuffix = Object.assign({}, wlanGroup);
        return copy;
    }

    /**
     * Convert a WlanGroupMySuffix to a JSON which can be sent to the server.
     */
    private convert(wlanGroup: WlanGroupMySuffix): WlanGroupMySuffix {
        const copy: WlanGroupMySuffix = Object.assign({}, wlanGroup);
        return copy;
    }
}
