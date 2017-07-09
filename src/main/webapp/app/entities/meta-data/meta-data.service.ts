import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { MetaData } from './meta-data.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class MetaDataService {

    private resourceUrl = 'api/meta-data';
    private resourceSearchUrl = 'api/_search/meta-data';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(metaData: MetaData): Observable<MetaData> {
        const copy = this.convert(metaData);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(metaData: MetaData): Observable<MetaData> {
        const copy = this.convert(metaData);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<MetaData> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
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

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.published = this.dateUtils
            .convertDateTimeFromServer(entity.published);
        entity.lastModified = this.dateUtils
            .convertDateTimeFromServer(entity.lastModified);
    }

    private convert(metaData: MetaData): MetaData {
        const copy: MetaData = Object.assign({}, metaData);

        copy.published = this.dateUtils.toDate(metaData.published);

        copy.lastModified = this.dateUtils.toDate(metaData.lastModified);
        return copy;
    }
}
