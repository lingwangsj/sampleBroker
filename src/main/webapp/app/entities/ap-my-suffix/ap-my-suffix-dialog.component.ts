import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';

import { Observable } from 'rxjs/Observable';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { APMySuffix } from './ap-my-suffix.model';
import { APMySuffixPopupService } from './ap-my-suffix-popup.service';
import { APMySuffixService } from './ap-my-suffix.service';
import { APGroupMySuffix, APGroupMySuffixService } from '../ap-group-my-suffix';

@Component({
    selector: 'jhi-ap-my-suffix-dialog',
    templateUrl: './ap-my-suffix-dialog.component.html'
})
export class APMySuffixDialogComponent implements OnInit {

    aP: APMySuffix;
    isSaving: boolean;

    apgroups: APGroupMySuffix[];

    constructor(
        public activeModal: NgbActiveModal,
        private jhiAlertService: JhiAlertService,
        private aPService: APMySuffixService,
        private aPGroupService: APGroupMySuffixService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.aPGroupService.query()
            .subscribe((res: HttpResponse<APGroupMySuffix[]>) => { this.apgroups = res.body; }, (res: HttpErrorResponse) => this.onError(res.message));
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

    private subscribeToSaveResponse(result: Observable<HttpResponse<APMySuffix>>) {
        result.subscribe((res: HttpResponse<APMySuffix>) =>
            this.onSaveSuccess(res.body), (res: HttpErrorResponse) => this.onSaveError());
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

    trackAPGroupById(index: number, item: APGroupMySuffix) {
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
