import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { APMySuffix } from './ap-my-suffix.model';
import { APMySuffixPopupService } from './ap-my-suffix-popup.service';
import { APMySuffixService } from './ap-my-suffix.service';

@Component({
    selector: 'jhi-ap-my-suffix-delete-dialog',
    templateUrl: './ap-my-suffix-delete-dialog.component.html'
})
export class APMySuffixDeleteDialogComponent {

    aP: APMySuffix;

    constructor(
        private aPService: APMySuffixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.aPService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'aPListModification',
                content: 'Deleted an aP'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-ap-my-suffix-delete-popup',
    template: ''
})
export class APMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private aPPopupService: APMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.aPPopupService
                .open(APMySuffixDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
