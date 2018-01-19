import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { WlanGroupMySuffix } from './wlan-group-my-suffix.model';
import { WlanGroupMySuffixPopupService } from './wlan-group-my-suffix-popup.service';
import { WlanGroupMySuffixService } from './wlan-group-my-suffix.service';

@Component({
    selector: 'jhi-wlan-group-my-suffix-delete-dialog',
    templateUrl: './wlan-group-my-suffix-delete-dialog.component.html'
})
export class WlanGroupMySuffixDeleteDialogComponent {

    wlanGroup: WlanGroupMySuffix;

    constructor(
        private wlanGroupService: WlanGroupMySuffixService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.wlanGroupService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'wlanGroupListModification',
                content: 'Deleted an wlanGroup'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-wlan-group-my-suffix-delete-popup',
    template: ''
})
export class WlanGroupMySuffixDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private wlanGroupPopupService: WlanGroupMySuffixPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.wlanGroupPopupService
                .open(WlanGroupMySuffixDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
