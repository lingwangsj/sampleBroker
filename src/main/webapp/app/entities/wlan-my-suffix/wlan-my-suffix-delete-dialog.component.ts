import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WlanMySuffix } from './wlan-my-suffix.model';
import { WlanMySuffixPopupService } from './wlan-my-suffix-popup.service';
import { WlanMySuffixService } from './wlan-my-suffix.service';

@Component({
    selector: 'jhi-wlan-my-suffix-delete-dialog',
    templateUrl: './wlan-my-suffix-delete-dialog.component.html'
})
export class WlanMySuffixDeleteDialogComponent {

    wlan: WlanMySuffix;

    constructor(
        private wlanService: WlanMySuffixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wlanService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wlanListModification',
                content: 'Deleted an wlan'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wlan-my-suffix-delete-popup',
    template: ''
})
export class WlanMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wlanPopupService: WlanMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wlanPopupService
                .open(WlanMySuffixDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
