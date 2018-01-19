import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { APMySuffix } from './ap-my-suffix.model';
import { APMySuffixPopupService } from './ap-my-suffix-popup.service';
import { APMySuffixService } from './ap-my-suffix.service';
import { WlanGroupMySuffix, WlanGroupMySuffixService } from '../wlan-group-my-suffix';
import { ZoneMySuffix, ZoneMySuffixService } from '../zone-my-suffix';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-ap-my-suffix-dialog',
    templateUrl: './ap-my-suffix-dialog.component.html'
})
export class APMySuffixDialogComponent implements OnInit {

    aP: APMySuffix;
    isSaving: boolean;

    wg24s: WlanGroupMySuffix[];

    wg50s: WlanGroupMySuffix[];

    zones: ZoneMySuffix[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private aPService: APMySuffixService,
        private wlanGroupService: WlanGroupMySuffixService,
        private zoneService: ZoneMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.wlanGroupService
            .query({filter: 'ap-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.aP.wg24 || !this.aP.wg24.id) {
                    this.wg24s = res.json;
                } else {
                    this.wlanGroupService
                        .find(this.aP.wg24.id)
                        .subscribe((subRes: WlanGroupMySuffix) => {
                            this.wg24s = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.wlanGroupService
            .query({filter: 'ap-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.aP.wg50 || !this.aP.wg50.id) {
                    this.wg50s = res.json;
                } else {
                    this.wlanGroupService
                        .find(this.aP.wg50.id)
                        .subscribe((subRes: WlanGroupMySuffix) => {
                            this.wg50s = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.zoneService.query()
            .subscribe((res: ResponseWrapper) => { this.zones = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.aP.id !== undefined) {
            this.subscribeToSaveResponse(
                this.aPService.update(this.aP));
        } else {
            this.subscribeToSaveResponse(
                this.aPService.create(this.aP));
        }
    }

    private subscribeToSaveResponse(result: Observable<APMySuffix>) {
        result.subscribe((res: APMySuffix) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: APMySuffix) {
        this.eventManager.broadcast({ name: 'aPListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackWlanGroupById(index: number, item: WlanGroupMySuffix) {
        return item.id;
    }

    trackZoneById(index: number, item: ZoneMySuffix) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-ap-my-suffix-popup',
    template: ''
})
export class APMySuffixPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private aPPopupService: APMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.aPPopupService
                    .open(APMySuffixDialogComponent as Component, params['id']);
            } else {
                this.aPPopupService
                    .open(APMySuffixDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
