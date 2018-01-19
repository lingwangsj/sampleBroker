/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable } from 'rxjs/Observable';
import { JhiEventManager } from 'ng-jhipster';

import { SampleBrokerTestModule } from '../../../test.module';
import { WlanGroupMySuffixDeleteDialogComponent } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix-delete-dialog.component';
import { WlanGroupMySuffixService } from '../../../../../../main/webapp/app/entities/wlan-group-my-suffix/wlan-group-my-suffix.service';

describe('Component Tests', () => {

    describe('WlanGroupMySuffix Management Delete Component', () => {
        let comp: WlanGroupMySuffixDeleteDialogComponent;
        let fixture: ComponentFixture<WlanGroupMySuffixDeleteDialogComponent>;
        let service: WlanGroupMySuffixService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [WlanGroupMySuffixDeleteDialogComponent],
                providers: [
                    WlanGroupMySuffixService
                ]
            })
            .overrideTemplate(WlanGroupMySuffixDeleteDialogComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(WlanGroupMySuffixDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(WlanGroupMySuffixService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete',
                inject([],
                    fakeAsync(() => {
                        // GIVEN
                        spyOn(service, 'delete').and.returnValue(Observable.of({}));

                        // WHEN
                        comp.confirmDelete(123);
                        tick();

                        // THEN
                        expect(service.delete).toHaveBeenCalledWith(123);
                        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                        expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                    })
                )
            );
        });
    });

});
