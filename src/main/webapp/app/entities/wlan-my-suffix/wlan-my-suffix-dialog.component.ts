import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WlanMySuffix } from './wlan-my-suffix.model';
import { WlanMySuffixPopupService } from './wlan-my-suffix-popup.service';
import { WlanMySuffixService } from './wlan-my-suffix.service';
import { ZoneMySuffix, ZoneMySuffixService } from '../zone-my-suffix';
import { WlanGroupMySuffix, WlanGroupMySuffixService } from '../wlan-group-my-suffix';

@Component({
    selector: 'jhi-wlan-my-suffix-dialog',
    templateUrl: './wlan-my-suffix-dialog.component.html'
})
export class WlanMySuffixDialogComponent implements OnInit {

    wlan: WlanMySuffix;
    isSaving: boolean;

    zones: ZoneMySuffix[];

    wlangroups: WlanGroupMySuffix[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private wlanService: WlanMySuffixService,
        private zoneService: ZoneMySuffixService,
        private wlanGroupService: WlanGroupMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.zoneService.query()
            .subscribe((res: HttpResponse<ZoneMySuffix[]>) => { this.zones = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
        this.wlanGroupService.query()
            .subscribe((res: HttpResponse<WlanGroupMySuffix[]>) => { this.wlangroups = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.wlan.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wlanService.update(this.wlan));
        } else {
            this.subscribeToSaveResponse(
                this.wlanService.create(this.wlan));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<WlanMySuffix>>) {
        result.subscribe((res: HttpResponse<WlanMySuffix>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: WlanMySuffix) {
        this.eventManager.broadcast({ name: 'wlanListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackZoneById(index: number, item: ZoneMySuffix) {
        return item.id;
    }

    trackWlanGroupById(index: number, item: WlanGroupMySuffix) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-wlan-my-suffix-popup',
    template: ''
})
export class WlanMySuffixPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wlanPopupService: WlanMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.wlanPopupService
                    .open(WlanMySuffixDialogComponent as Component, params['id']);
            } else {
                this.wlanPopupService
                    .open(WlanMySuffixDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
