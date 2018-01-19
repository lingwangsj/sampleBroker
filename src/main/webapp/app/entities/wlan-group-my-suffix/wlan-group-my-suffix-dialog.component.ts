import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { WlanGroupMySuffix } from './wlan-group-my-suffix.model';
import { WlanGroupMySuffixPopupService } from './wlan-group-my-suffix-popup.service';
import { WlanGroupMySuffixService } from './wlan-group-my-suffix.service';
import { WlanMySuffix, WlanMySuffixService } from '../wlan-my-suffix';
import { ZoneMySuffix, ZoneMySuffixService } from '../zone-my-suffix';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-wlan-group-my-suffix-dialog',
    templateUrl: './wlan-group-my-suffix-dialog.component.html'
})
export class WlanGroupMySuffixDialogComponent implements OnInit {

    wlanGroup: WlanGroupMySuffix;
    isSaving: boolean;

    wlans: WlanMySuffix[];

    zones: ZoneMySuffix[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private wlanGroupService: WlanGroupMySuffixService,
        private wlanService: WlanMySuffixService,
        private zoneService: ZoneMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.wlanService.query()
            .subscribe((res: ResponseWrapper) => { this.wlans = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.zoneService.query()
            .subscribe((res: ResponseWrapper) => { this.zones = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.wlanGroup.id !== undefined) {
            this.subscribeToSaveResponse(
                this.wlanGroupService.update(this.wlanGroup));
        } else {
            this.subscribeToSaveResponse(
                this.wlanGroupService.create(this.wlanGroup));
        }
    }

    private subscribeToSaveResponse(result: Observable<WlanGroupMySuffix>) {
        result.subscribe((res: WlanGroupMySuffix) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError());
    }

    private onSaveSuccess(result: WlanGroupMySuffix) {
        this.eventManager.broadcast({ name: 'wlanGroupListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackWlanById(index: number, item: WlanMySuffix) {
        return item.id;
    }

    trackZoneById(index: number, item: ZoneMySuffix) {
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
    selector: 'jhi-wlan-group-my-suffix-popup',
    template: ''
})
export class WlanGroupMySuffixPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wlanGroupPopupService: WlanGroupMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.wlanGroupPopupService
                    .open(WlanGroupMySuffixDialogComponent as Component, params['id']);
            } else {
                this.wlanGroupPopupService
                    .open(WlanGroupMySuffixDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
