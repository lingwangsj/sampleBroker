import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { ZoneMySuffix } from './zone-my-suffix.model';
import { ZoneMySuffixPopupService } from './zone-my-suffix-popup.service';
import { ZoneMySuffixService } from './zone-my-suffix.service';
import { DomainMySuffix, DomainMySuffixService } from '../domain-my-suffix';

@Component({
    selector: 'jhi-zone-my-suffix-dialog',
    templateUrl: './zone-my-suffix-dialog.component.html'
})
export class ZoneMySuffixDialogComponent implements OnInit {

    zone: ZoneMySuffix;
    isSaving: boolean;

    domains: DomainMySuffix[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private zoneService: ZoneMySuffixService,
        private domainService: DomainMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.domainService.query()
            .subscribe((res: HttpResponse<DomainMySuffix[]>) => { this.domains = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.zone.id !== undefined) {
            this.subscribeToSaveResponse(
                this.zoneService.update(this.zone));
        } else {
            this.subscribeToSaveResponse(
                this.zoneService.create(this.zone));
        }
    }

    private subscribeToSaveResponse(result: Observable<HttpResponse<ZoneMySuffix>>) {
        result.subscribe((res: HttpResponse<ZoneMySuffix>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess(result: ZoneMySuffix) {
        this.eventManager.broadcast({ name: 'zoneListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError() {
        this.isSaving = false;
    }

    private onError(error: any) {
        this.jhiAlertService.error(error.message, null, null);
    }

    trackDomainById(index: number, item: DomainMySuffix) {
        return item.id;
    }
}

@Component({
    selector: 'jhi-zone-my-suffix-popup',
    template: ''
})
export class ZoneMySuffixPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private zonePopupService: ZoneMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.zonePopupService
                    .open(ZoneMySuffixDialogComponent as Component, params['id']);
            } else {
                this.zonePopupService
                    .open(ZoneMySuffixDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
