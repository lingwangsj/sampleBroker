import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { SERVER_API_URL } from '../../app.constants';

import { ZoneMySuffix } from './zone-my-suffix.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ZoneMySuffixService {

    private resourceUrl =  SERVER_API_URL + 'api/zones';

    constructor(private http: Http) { }

    create(zone: ZoneMySuffix): Observable<ZoneMySuffix> {
        const copy = this.convert(zone);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(zone: ZoneMySuffix): Observable<ZoneMySuffix> {
        const copy = this.convert(zone);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<ZoneMySuffix> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        for (let i = 0; i < jsonResponse.length; i++) {
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to ZoneMySuffix.
     */
    private convertItemFromServer(json: any): ZoneMySuffix {
        const entity: ZoneMySuffix = Object.assign(new ZoneMySuffix(), json);
        return entity;
    }

    /**
     * Convert a ZoneMySuffix to a JSON which can be sent to the server.
     */
    private convert(zone: ZoneMySuffix): ZoneMySuffix {
        const copy: ZoneMySuffix = Object.assign({}, zone);
        return copy;
    }
}
