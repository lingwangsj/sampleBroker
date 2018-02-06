import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { APGroupMySuffix } from './ap-group-my-suffix.model';
import { APGroupMySuffixPopupService } from './ap-group-my-suffix-popup.service';
import { APGroupMySuffixService } from './ap-group-my-suffix.service';
import { WlanGroupMySuffix, WlanGroupMySuffixService } from '../wlan-group-my-suffix';
import { ZoneMySuffix, ZoneMySuffixService } from '../zone-my-suffix';

@Component({
    selector: 'jhi-ap-group-my-suffix-dialog',
    templateUrl: './ap-group-my-suffix-dialog.component.html'
})
export class APGroupMySuffixDialogComponent implements OnInit {

    aPGroup: APGroupMySuffix;
    isSaving: boolean;

    wg24s: WlanGroupMySuffix[];

    wg50s: WlanGroupMySuffix[];

    zones: ZoneMySuffix[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private aPGroupService: APGroupMySuffixService,
        private wlanGroupService: WlanGroupMySuffixService,
        private zoneService: ZoneMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.wlanGroupService
            .query({filter: 'apgroup-is-null'})
            .subscribe((res: HttpResponse<WlanGroupMySuffix[]>) => {
                if (!this.aPGroup.wg24 || !this.aPGroup.wg24.id) {
                    this.wg24s = res.body;
                } else {
                    this.wlanGroupService
                        .find(this.aPGroup.wg24.id)
                        .subscribe((subRes: HttpResponse<WlanGroupMySuffix>) => {
                            this.wg24s = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.wlanGroupService
            .query({filter: 'apgroup-is-null'})
            .subscribe((res: HttpResponse<WlanGroupMySuffix[]>) => {
                if (!this.aPGroup.wg50 || !this.aPGroup.wg50.id) {
                    this.wg50s = res.body;
                } else {
                    this.wlanGroupService
                        .find(this.aPGroup.wg50.id)
                        .subscribe((subRes: HttpResponse<WlanGroupMySuffix>) => {
                            this.wg50s = [subRes.body].concat(res.body);
                        }, (subRes: HttpErrorResponse) => this.onError(subRes.message));
                }
            }, (res: HttpErrorResponse) => this.onError(res.message));
        this.zoneService.query()
            .subscribe((res: HttpResponse<ZoneMySuffix[]>) => { this.zones = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.aPGroup.id !== undefined) {
            this.subscribeToSaveResponse(
                this.aPGroupService.update(this.aPGroup));
        } else {
            this.subscribeToSaveResponse(
                this.aPGroupService.create(this.aPGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<APGroupMySuffix>>) {
        result.subscribe((res: HttpResponse<APGroupMySuffix>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: APGroupMySuffix) {
        this.eventManager.broadcast({ name: 'aPGroupListModification', content: 'OK'});
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
    selector: 'jhi-ap-group-my-suffix-popup',
    template: ''
})
export class APGroupMySuffixPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private aPGroupPopupService: APGroupMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.aPGroupPopupService
                    .open(APGroupMySuffixDialogComponent as Component, params['id']);
            } else {
                this.aPGroupPopupService
                    .open(APGroupMySuffixDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
